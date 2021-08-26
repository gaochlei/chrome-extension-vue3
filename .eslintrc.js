module.exports = {
  root: true,
  env: {
    node: true,
    webextensions: true
  },
  extends: [
    'plugin:vue/essential',
    '@vue/standard'
  ],
  parserOptions: {
    parser: 'babel-eslint'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'camelcase': 'off',
    'no-multi-spaces': 'off',
    'eqeqeq': 'off',
    'no-undef': 'off',
    'dot-notation': 'off',
    'no-unused-vars': 'off',
    'comma-spacing': 'off',
    'quotes': 'off',
    'semi': 'off',
    'comma-dangle': 'off',
    'space-before-function-paren': 'off',
    'spaced-comment': 'off',
    'space-before-blocks': 'off',
    'keyword-spacing': 'off',
    'space-infix-ops': 'off',
    'no-array-constructor': 'off',
    'padded-blocks': 'off',
    'brace-style': 'off',
    'one-var': 'off',
    'curly': 'off',
    'operator-linebreak': 'off',
    'space-in-parens': 'off',
    'block-spacing': 'off',
  }
}
