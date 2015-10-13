'use strict'; 
var util = require('util'),
    path = require('path'),
    yeoman = require('yeoman-generator'),
    chalk = require('chalk'),
    _ = require('underscore.string'),
    shelljs = require('shelljs'),
    scriptBase = require('../script-base'),
    packagejs = require(__dirname + '/../package.json'),
    crypto = require("crypto"),
    mkdirp = require('mkdirp'),
    html = require("html-wiring"),
    ejs = require('ejs');

var ScrippsGenerator = module.exports = function ScrippsGenerator(args, options, config) {

    yeoman.generators.Base.apply(this, arguments);

    this.pkg = JSON.parse(html.readFileAsString(path.join(__dirname, '../package.json')));
};

util.inherits(ScrippsGenerator, yeoman.generators.Base);
util.inherits(ScrippsGenerator, scriptBase);

ScrippsGenerator.prototype.askFor = function askFor() {
    var cb = this.async();

    console.log(chalk.red('\n' +
        '              _    __    _       __        ___   ____  _      __        \n' +
        '             | |  / /\\  \\ \\  /  / /\\      | | \\ | |_  \\ \\  / ( (`       \n' +
        '           \\_|_| /_/--\\  \\_\\/  /_/--\\     |_|_/ |_|__  \\_\\/  _)_)       \n'));

    console.log('\nWelcome to the Scripps Generator v' + packagejs.version + '\n');
    var insight = this.insight();
    var questions = 8; // making questions a variable to avoid updating each question by hand when adding additional options

     var prompts = [
        {
            type: 'input',
            name: 'baseName',
            validate: function (input) {
                if (/^([a-zA-Z0-9_]*)$/.test(input)) return true;
                return 'Your application name cannot contain special characters or a blank space, using the default name instead';
            },
            message: '(1/' + questions + ') What is the base name of your application?',
            default: 'test'
        },
        {
            type: 'input',
            name: 'packageName',
            validate: function (input) {
                if (/^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/.test(input)) return true;
                return 'The package name you have provided is not a valid Java package name.';
            },
            message: '(2/' + questions + ') What is your default Java package name?',
            default: 'com.tek.myservice'
        },
        {
            type: 'input',
            name: 'indexname',
            validate: function (input) {
                if (/^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/.test(input)) return true;
                return 'The package name you have provided is not a valid index name.';
            },
            message: '(2/' + questions + ') What is your default index name?',
            default: 'myindex'
        },
        {
            type: 'input',
            name: 'dockerreponame',
            validate: function (input) {
                if (/^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/.test(input)) return true;
                return 'The package name you have provided is not a valid Java package name.';
            },
            message: '(3/' + questions + ') What is your Docker repo?',
            default: 'reasonthearchitect'
        },
        {
            type: 'input',
            name: 'subreponame',
            validate: function (input) {
                if (/^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/.test(input)) return true;
                return 'The subrepo name you have provided is not a valid Java package name.';
            },
            message: '(4/' + questions + ') What is your Docker subrepo name?',
            default: 'sub'
        },
        {
            type: 'input',
            name: 'ports',
            message: '(5/' + questions + ') What ports do you want to address?',
            default: '8080'
        },
        {
            type: 'input',
            name: 's3bucket',
            message: '(6/' + questions + ') What is the name of your s3 bucket?',
            default: 'test'
        }

    ];



    this.baseName               = this.config.get('baseName');
    this.packageName            = this.config.get('packageName');
    this.indexname            = this.config.get('indexname');
    this.dockerreponame         = this.config.get('dockerreponame');
    this.subreponame            = this.config.get('subreponame');
    this.ports                  = this.config.get('ports');
    this.s3bucket               = this.config.get('s3bucket');

    this.packageNameGenerated   = '';
    var generated               = "generated";
    
    
    this.packagejs = packagejs;

    if (this.baseName != null &&
        this.packageName != null &&
        this.dockerreponame != null
        ) {
            console.log(chalk.green('This is an existing project, using the configuration from your .yo-rc.json file \n' +
            'to re-generate the project...\n'));

        cb();
    } else {
        this.prompt(prompts, function (props) {
            if (props.insight !== undefined) {
                insight.optOut = !props.insight;
            }
            this.baseName               = props.baseName;
            this.elastic                = "yes";
            this.packageName            = props.packageName;
            this.dockerreponame         = props.dockerreponame;
            this.indexname              = props.indexname;
            this.subreponame            = props.subreponame;
            this.ports                  = props.ports;
            this.s3bucket               = props.s3bucket;
            var generated               = ".generated";
            this.packageNameGenerated   = props.packageName +  generated;
            console.log(this.packageNameGenerated);
            
            cb();
        }.bind(this));
    }
};

ScrippsGenerator.prototype.app = function app() {
    var insight = this.insight();
    insight.track('generator', 'app');
    insight.track('app/searchEngine', this.searchEngine);

    var packageFolder = this.packageName.replace(/\./g, '/');
    var javaDir = 'src/main/java/' + packageFolder + '/';
    var groovyItTest = 'src/integration/groovy/' + packageFolder + '/it/';
    var resourceDir = 'src/main/resources/';
    var conf = "conf/"
    var webappDir = 'src/main/webapp/';
    var interpolateRegex = /<%=([\s\S]+?)%>/g; // so that tags in templates do not get mistreated as _ templates

    // Remove old files
    setUoCircleCi(this);

    //placeholders for groovy.
    this.copy('placeholder', 'src/main/groovy/placeholder');
    //this.copy('placeholder', 'src/test/groovy/placeholder');
    this.template('src/integration/groovy/package/it/_AbstractItTest.groovy', groovyItTest + 'AbstractItTest.groovy', this, {});
    //AbstractItTest
    this.copy('src/test/groovy/placeholder','src/test/groovy/placeholder');



    // Note that both these are due to bugs in the Spring/Liquibase/Swagger solutions,
    this.copy(resourceDir + '/templates/error.html', resourceDir + 'templates/error.html');
    this.copy('src/main/webapp/README.md', 'src/main/webapp/README.md');

    // Application name modified, using each technology's conventions
    this.angularAppName = _.camelize(_.slugify(this.baseName)) + 'App';
    this.camelizedBaseName = _.camelize(this.baseName);
    this.slugifiedBaseName = _.slugify(this.baseName);

    // Create application
    this.template('_README.md', 'README.md', this, {});
    this.copy('gitignore', '.gitignore');
    this.copy('gitattributes', '.gitattributes');
    
    this.template('_dynamodb-titan-storage-backend-cfn.json', 'dynamodb-titan-storage-backend-cfn.json', this, {});
    
        

    this.template('_circle.yml', 'circle.yml', this, {});

    this.copy('src/main/docker/_Dockerfile', 'src/main/docker/Dockerfile');
    // Create the conf folder.
    mkdirp(conf);

    setUpGradle(this);
    setUpJbehave(this, packageFolder);
    
    // Create Java resource files
    mkdirp(resourceDir);
    this.copy(resourceDir + '/banner.txt', resourceDir + '/banner.txt');
    // Thymeleaf templates

    this.template(resourceDir + '_logback.xml', resourceDir + 'logback.xml', this, {'interpolate': interpolateRegex});

    this.template(resourceDir + '/config/_application.yml', resourceDir + 'config/application.yml', this, {});
    this.template(resourceDir + '/config/_application-dev.yml', resourceDir + 'config/application-dev.yml', this, {});
    this.template(resourceDir + '/config/_application-prod.yml', resourceDir + 'config/application-prod.yml', this, {});

    // Create Java files
    this.template('src/main/java/package/_Application.java', javaDir + '/Application.java', this, {});
    //this.template('src/main/java/package/_MakeMeHappy.groovy', javaDir + '/MakeMeHappy.groovy', this, {});
    this.template('src/main/java/package/config/_Constants.java', javaDir + 'generated/config/Constants.java', this, {});
    //this.template('src/main/java/package/_ApplicationWebXml.java', javaDir + '/ApplicationWebXml.java', this, {});
    this.template('src/main/java/package/aop/logging/_LoggingAspect.java', javaDir + 'generated/aop/logging/LoggingAspect.java', this, {});
    this.template('src/main/java/package/config/apidoc/_SwaggerConfiguration.java', javaDir + 'generated/config/apidoc/SwaggerConfiguration.java', this, {});

    this.template('src/main/java/package/async/_ExceptionHandlingAsyncTaskExecutor.java', javaDir + 'generated/async/ExceptionHandlingAsyncTaskExecutor.java', this, {});
    this.template('src/main/java/package/config/_AsyncConfiguration.java', javaDir + 'generated/config/AsyncConfiguration.java', this, {});

    
    this.template('src/main/java/package/config/_JacksonConfiguration.java', javaDir + 'generated/config/JacksonConfiguration.java', this, {});
    this.template('src/main/java/package/config/_LoggingAspectConfiguration.java', javaDir + 'generated/config/LoggingAspectConfiguration.java', this, {});
    this.template('src/main/java/package/config/_MetricsConfiguration.java', javaDir + 'generated/config/MetricsConfiguration.java', this, {});


    // error handler code - server side
    this.template('src/main/java/package/web/rest/errors/_ErrorConstants.java', javaDir + 'generated/web/rest/errors/ErrorConstants.java', this, {});
    this.template('src/main/java/package/web/rest/errors/_CustomParameterizedException.java', javaDir + 'generated/web/rest/errors/CustomParameterizedException.java', this, {});
    this.template('src/main/java/package/web/rest/errors/_ErrorDTO.java', javaDir + 'generated/web/rest/errors/ErrorDTO.java', this, {});
    //this.template('src/main/java/package/web/rest/errors/_ExceptionTranslator.java', javaDir + 'web/rest/errors/ExceptionTranslator.java', this, {});
    this.template('src/main/java/package/web/rest/errors/_FieldErrorDTO.java', javaDir + 'generated/web/rest/errors/FieldErrorDTO.java', this, {});
    this.template('src/main/java/package/web/rest/errors/_ParameterizedErrorDTO.java', javaDir + 'generated/web/rest/errors/ParameterizedErrorDTO.java', this, {});


    console.log('*********************************************************************');
    console.log('');
    console.log('please add the loggin rest endpoint when you have the time.');
    console.log('');
    console.log('*********************************************************************');

    //this.template('src/main/java/package/web/rest/_LogsRest.java', javaDir + 'generated/web/rest/LogsRest.java', this, {});
    //this.template('src/main/java/package/web/rest/dto/_LoggerDto.java', javaDir + 'generated/web/rest/dto/LoggerDto.java', this, {});

    this.template('src/main/java/package/domain/util/_CustomLocalDateSerializer.java', javaDir + 'generated/domain/util/CustomLocalDateSerializer.java', this, {});
    this.template('src/main/java/package/domain/util/_CustomDateTimeSerializer.java', javaDir + 'generated/domain/util/CustomDateTimeSerializer.java', this, {});
    this.template('src/main/java/package/domain/util/_CustomDateTimeDeserializer.java', javaDir + 'generated/domain/util/CustomDateTimeDeserializer.java', this, {});
    this.template('src/main/java/package/domain/util/_ISO8601LocalDateDeserializer.java', javaDir + 'generated/domain/util/ISO8601LocalDateDeserializer.java', this, {});

    this.template('src/main/java/package/web/rest/util/_HeaderUtil.java', javaDir + 'generated/web/rest/util/HeaderUtil.java', this, {});
    this.template('src/main/java/package/web/rest/util/_PaginationUtil.java', javaDir + 'generated/web/rest/util/PaginationUtil.java', this, {});
        
    this.template('src/main/java/package/config/_ElasticConfiguration.java', javaDir + 'generated/config/ElasticConfiguration.java', this, {});
    //this.template('src/main/java/package/config/_TitanConfiguration.java', javaDir + 'generated/config/TitanConfiguration.java', this, {});
        
    //this.template('src/main/java/package/security/_Http401UnauthorizedEntryPoint.java', javaDir + 'security/Http401UnauthorizedEntryPoint.java', this, {});
    
    // Create Test Java files
    //var testDir = 'src/test/java/' + packageFolder + '/';
    var testResourceDir = 'src/test/resources/';
    //mkdirp(testDir);

    this.template(testResourceDir + 'config/_application.yml', testResourceDir + 'config/application.yml', this, {});
    this.template(testResourceDir + '_logback-test.xml', testResourceDir + 'logback-test.xml', this, {});

   

/*

    // Create Webapp
    mkdirp(webappDir);
*/
    this.config.set('baseName',             this.baseName);
    this.config.set('packageName',          this.packageName);
    this.config.set('packageNameGenerated', this.packageNameGenerated);
    this.config.set('packageFolder',        packageFolder);
    this.config.set('dockerreponame',       this.dockerreponame);
    this.config.set('indexname',            this.indexname);


    this.config.set('subreponame',          this.subreponame);
    this.config.set('ports',                this.ports);
    this.config.set('s3bucket',             this.s3bucket);

};

function setUpGradle(thing) {

    thing.template('_build.gradle', 'build.gradle', this, {});
    thing.template('_settings.gradle', 'settings.gradle', this, {});

    thing.copy('gradlew', 'gradlew');
    thing.template('_gradle.properties', 'gradle.properties', thing, {});
    
    thing.copy('gradlew.bat', 'gradlew.bat');
    thing.copy('gradle/wrapper/gradle-wrapper.jar', 'gradle/wrapper/gradle-wrapper.jar');
    thing.copy('gradle/wrapper/gradle-wrapper.properties', 'gradle/wrapper/gradle-wrapper.properties');

    //gradle
    thing.template('gradle/conf/_aws.gradle', 'gradle/conf/aws.gradle', thing, {});
    //thing.template('gradle/conf/_titan.gradle', 'gradle/conf/titan.gradle', thing, {});
    
    thing.template('gradle/conf/test/sonar.gradle', 'gradle/conf/test/sonar.gradle', thing, {});
    thing.template('gradle/conf/_docker.gradle', 'gradle/conf/docker.gradle', thing, {});
    thing.copy('gradle/conf/ide.gradle', 'gradle/conf/ide.gradle');
    thing.copy('gradle/conf/metrics.gradle', 'gradle/conf/metrics.gradle');
    thing.copy('gradle/conf/boot.gradle', 'gradle/conf/boot.gradle');
    thing.copy('gradle/conf/jackson.gradle', 'gradle/conf/jackson.gradle');
    thing.copy('gradle/conf/meta.gradle', 'gradle/conf/meta.gradle');
    thing.copy('gradle/conf/groovy.gradle', 'gradle/conf/groovy.gradle');
    thing.copy('gradle/conf/lombok.gradle', 'gradle/conf/lombok.gradle');
    thing.copy('gradle/conf/test/unit.gradle', 'gradle/conf/test/unit.gradle');
    thing.copy('gradle/conf/test/integration.gradle', 'gradle/conf/test/integration.gradle');
    thing.copy('gradle/conf/test/jbehave.gradle', 'gradle/conf/test/jbehave.gradle');
    thing.copy('gradle/conf/utils.gradle', 'gradle/conf/utils.gradle');
    // profiles
    thing.copy('gradle/conf/profiles/profile_dev.gradle', 'gradle/conf/profiles/profile_dev.gradle');
    thing.copy('gradle/conf/profiles/profile_prod.gradle', 'gradle/conf/profiles/profile_prod.gradle');
    thing.copy('gradle/conf/profiles/profile_fast.gradle', 'gradle/conf/profiles/profile_fast.gradle');
}

function setUpJbehave(thing, packageFolder) {

    var srcJavaDir = 'src/jbehave/java/package/jbehave/';
    var targetJavaDir = 'src/jbehave/java/' + packageFolder + '/jbehave/';

    thing.template(srcJavaDir + '_AbstractSpringJBehaveStory.java',       targetJavaDir + 'AbstractSpringJBehaveStory.java', this, {});
    thing.template(srcJavaDir + '_AcceptanceTest.java',                   targetJavaDir + 'AcceptanceTest.java', this, {});
    thing.template(srcJavaDir + '_AcceptanceTestsConfiguration.java',     targetJavaDir + 'AcceptanceTestsConfiguration.java', this, {});
    thing.template(srcJavaDir + '_Steps.java',                            targetJavaDir + 'Steps.java', this, {});

    var srcGroovyPackage = 'src/jbehave/groovy/package/jbehave/facade/';
    var targetGroovyPackage = 'src/jbehave/groovy/' + packageFolder + '/jbehave/facade/';
    thing.template(srcGroovyPackage + '_ExampleOfHowToBehave.groovy',     targetGroovyPackage + 'ExampleOfHowToBehave.groovy', this, {}); 

    var srcStoryPackage = 'src/jbehave/stories/package/jbehave/facade/';
    var targetStoryPackage = 'src/jbehave/stories/' + packageFolder + '/jbehave/facade/';
    thing.template( srcStoryPackage + '_example_of_how_to_behave.story',     targetStoryPackage  + 'example_of_how_to_behave.story', this, {}); 

       
}//6381

function setUoCircleCi(thing) {
    thing.template('_circle.yml', 'circle.yml', this, {});
    thing.template('s3/_Dockerrun.aws.json.template', 's3/Dockerrun.aws.json.template', this, {});
    thing.template('s3/_create_docker_run_file.sh', 's3/create_docker_run_file.sh', this, {});
}


ScrippsGenerator.prototype._injectDependenciesAndConstants = function _injectDependenciesAndConstants() {
    if (this.options['skip-install']) {
        this.log(
            'After running `npm install & bower install`, inject your front end dependencies' +
            '\ninto your source code by running:' +
            '\n' +
            '\n' + chalk.yellow.bold('grunt wiredep') +
            '\n' +
            '\n ...and generate the Angular constants with:' +
            '\n' + chalk.yellow.bold('grunt ngconstant:dev')
        );
    } 
};
