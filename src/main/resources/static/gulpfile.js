var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var reactify = require('reactify');

gulp.task('browserify', function () {
    return browserify('./src/index.js', {
        debug: true,
        transform: [reactify]
    })
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(gulp.dest('./'));
});

gulp.task('watch', function () {
    gulp.watch('src/*.js', ['browserify']);
});
