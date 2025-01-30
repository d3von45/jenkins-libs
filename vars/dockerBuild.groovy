def call(Map config = [:]) {
    def defaults = [
      imageName: 'app',
      dockerfile: 'Dockerfile',
      buildArgs: '',
      tags: ['latest']
    ]
    
    config = defaults + config

    docker.build("${config.imageName}:${config.tags[0]}", "--file ${config.dockerfile} ${config.buildArgs} .")

    for(tag in config.tags.drop(1)){
        docker.image("${config.imageName}:${config.tags[0]}").push(tag)
    }
}
