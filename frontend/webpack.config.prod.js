const webpack = require('webpack');

const {merge} = require('webpack-merge');
const path = require('path');

const HtmlWebPackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const config = require('./webpack.config');

const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

const appConfig = require('./src/utils/config/webpack');
 
module.exports = merge(config, {
  mode: 'production',
  entry: './src/index.js',
  output: {
    filename: `integracaoponto-main-min.js`,
    chunkFilename: 'integracaoponto-[name]-bundle-min.js',
    path: path.join(__dirname, 'dist'),
  },
  plugins: [
    new CleanWebpackPlugin(),
    new webpack.DefinePlugin({
      DEBUG: JSON.stringify(false),
      MOCK: JSON.stringify(false),
      DIST: JSON.stringify(appConfig.dist)
    }),
    new MiniCssExtractPlugin({
      filename: 'integracaoponto-[name]-min.css',
      chunkFilename: 'integracaoponto-[id]-[hash]-min.css'
    }),
    new HtmlWebPackPlugin({
      template: './public/index.html',
      filename: 'index.html'
    })
  ]
});
