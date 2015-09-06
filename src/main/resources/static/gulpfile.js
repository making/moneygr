var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var babelify = require('babelify');
var drFrankenstyle = require('dr-frankenstyle');

gulp.task('browserify', function () {
    return browserify('./src/index.js', {
        debug: true,
        transform: [babelify]
    })
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(gulp.dest('./'));
});

gulp.task('css', function() {
    return drFrankenstyle()
        .pipe(gulp.dest('./css'));
});

gulp.task('watch', function () {
    gulp.watch('src/*.js', ['browserify']);
});
