var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var babelify = require('babelify');
var drFrankenstyle = require('dr-frankenstyle');

var srcDir = './src/main/js';
var staticDir = './src/main/resources/static';

gulp.task('browserify', function () {
    return browserify(srcDir + '/index.js', {
        debug: true,
        transform: [babelify]
    })
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(gulp.dest(staticDir + '/js'));
});

gulp.task('css', function () {
    return drFrankenstyle()
        .pipe(gulp.dest(staticDir + '/css'));
});

gulp.task('watch', function () {
    gulp.watch(srcDir + '/*.js', ['browserify']);
    gulp.watch(srcDir + '/**/*.jsx', ['browserify']);
});
