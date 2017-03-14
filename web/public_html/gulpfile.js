/// <binding />
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
                'bower_components/angular-route/angular-route.js',
                'bower_components/less/dist/less.js'
            ])
            .pipe(concat('base.js'))
            .pipe(gulp.dest('js/'));
});

gulp.task('appjs', function () {
    return gulp.src(
            [
                'app_components/angular/app.js'
            ])
            .pipe(concat('app.js'))
            .pipe(gulp.dest('js/'));
});

var less = require('gulp-less');
gulp.task('less', function () {
    return gulp.src(
            [
                'app_components/less/login.less',
                'app_components/less/master.less'
            ])
            .pipe(less('lib.less'))
            .pipe(gulp.dest('estilos/'));
});

var css = require('gulp-css');
gulp.task('css', function () {
    return gulp.src(
            [
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