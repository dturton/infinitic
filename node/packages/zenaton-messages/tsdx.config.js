const copy = require('rollup-plugin-copy');

module.exports = {
  rollup(config, options) {
    config.plugins.push(
      copy({
        targets: [
          {
            src: 'src/avro/jobManager/messages/envelopes/*.avsc',
            dest: 'dist/avro/jobManager/messages/envelopes',
          },
          {
            src: 'src/avro/jobManager/messages/*.avsc',
            dest: 'dist/avro/jobManager/messages',
          },
          {
            src: 'src/avro/common/data/*.avsc',
            dest: 'dist/avro/common/data',
          },
        ],
      })
    );

    return config;
  }
};
