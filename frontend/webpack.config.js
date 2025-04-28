const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.jsx']
  },
  entry: './src/index.tsx',
  module: { 
    rules: [
      {
        test: /\.js$/,
        use: ['babel-loader', 'source-map-loader'],
        exclude: /node_modules/,
      },
      {
        test: /\.tsx?$/,
        use: ['babel-loader', 'ts-loader'],
      },
      {
        test: /\.css$/,
        use: [
          MiniCssExtractPlugin.loader, 
          { loader: 'css-loader', options: { importLoaders: 0 } }
        ],
      },
      {
        test: /\.(scss|sass)$/,
        loaders: [
          MiniCssExtractPlugin.loader,
          { loader: 'css-loader', options: { importLoaders: 0 } },
          'sass-loader'
        ]
      },
      {
        test: /\.(png|jp(e*)g|svg)$/,
        use: [{
          loader: 'url-loader',
          options: {
            limit: 8000, // Convert images < 8kb to base64 strings
            name: './img/[name]-[hash].[ext]'
          }
        }]
      }
    ]
  }
};

