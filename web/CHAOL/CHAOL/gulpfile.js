/// <binding BeforeBuild='angular-js, less, js' />
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');

gulp.task('js', function () {
    return gulp.src(
        [
            'bower_components/jquery/dist/jquery.js',
            'bower_components/bootstrap/dist/js/bootstrap.js',
            'bower_components/angular/angular.js',
            'bower_components/angular-animate/angular-animate.js',
            'bower_components/angular-aria/angular-aria.js',
            'bower_components/angular-messages/angular-messages.js',
            'bower_components/angular-route/angular-route.js',
            'bower_components/angular-material/angular-material.js',
            'bower_components/angular-sanitize/angular-sanitize.js',
            'bower_components/angular-route-styles/route-styles.js',
            'bower_components/firebase/firebase.js',
            'bower_components/angularfire/dist/angularfire.js',
            'bower_components/less/dist/less.js'
        ])
        .pipe(concat('base.js'))
        .pipe(gulp.dest('js/'));
});

gulp.task('angular-js', function () {
    return gulp.src(
        [
            'app_components/angular/app.js',
            'app_components/angular/config.js',
            'app_components/angular/services.js',
            'app_components/angular/components.js',
            'app_components/angular/loginController.js',
            'app_components/angular/registroController.js',
            'app_components/angular/recuperarContrasenaController.js',
            'app_components/angular/menuController.js',
            'app_components/angular/modulos/inicioController.js',
            'app_components/angular/modulos/administracion/clientes/clientesController.js'
        ])
        .pipe(concat('app.js'))
        .pipe(gulp.dest('js/'));
});

gulp.task('tools-js', function () {
    return gulp.src(
        [
            'app_components/js/bootstrap-calendar/bootstrap-year-calendar.js',
            'app_components/js/bootstrap-calendar/bootstrap-year-calendar.es.js',
            'app_components/js/bootstrap-calendar/calendario-init.js',
        ])
        .pipe(concat('tools.js'))
        .pipe(gulp.dest('js/'));
});

var less = require('gulp-less');
gulp.task('less', function () {
    return gulp.src(
        [
            'app_components/less/login.less',
            'app_components/less/registro.less',
            'app_components/less/master.less'
        ])
        .pipe(less('lib.less'))
        .pipe(gulp.dest('estilos/'));
});

gulp.task('css', function () {
    return gulp.src(
        [
            'bower_components/angular-material/angular-material.css',
            'bower_components/bootstrap/dist/css/bootstrap.css',
            'bower_components/bootstrap/dist/css/bootstrap-theme.css'
        ])
        .pipe(concat('base.css'))
        .pipe(gulp.dest('estilos/'));
});

gulp.task('icons', function () {
    return gulp.src('bower_components/font-awesome/fonts/**.*')

        .pipe(gulp.dest('fuentes/'));
});