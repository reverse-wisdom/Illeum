module.exports = {
  lintOnSave: false,
  productionSourceMap: false,
  filenameHashing: true,
  publicPath: '',

  devServer: {
    hot: true,
    proxy: {
      '/api': {
        target: 'https://k4d106.p.ssafy.io',
        // target: 'http://k4d106.p.ssafy.io:8080',
      },
      'http://localhost:8000/': {
        target: 'http://k4d106.p.ssafy.io:8000',
        changeOrigin: true,
      },
    },
  },
  css: {
    loaderOptions: {
      css: {
        sourceMap: process.env.NODE_ENV !== 'production' ? true : false,
      },
    },
  },
};
