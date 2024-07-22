# Biblioteca EasyDev Worker

A classe `Worker` é uma classe utilitária para gerenciar tarefas em segundo plano no Android. Ela permite definir ações a serem executadas antes e depois das tarefas, manipular feedback das tarefas e gerenciar a execução das mesmas.

## Funcionalidades
- Executar tarefas em segundo plano
- Definir ações a serem realizadas antes e depois da tarefa
- Monitorar o progresso e o status da tarefa
- Manipular resultados e erros das tarefas

## Instalação
Adicione a seguinte dependência ao seu arquivo `build.gradle`:
```gradle
dependencies {
    implementation 'com.ceas.develop:easydev:1.0.0'
}

Uso
Criando um Worker
Para criar um Worker, utilize o método Worker.get() e passe uma implementação da interface Work. A interface Work possui um único método working() que contém a implementação da tarefa.

Worker<String, Integer> worker = Worker.get(new Work<String, Integer>() {
    @Override
    public Integer working(Manager manager, String... params) {
        // Implementação da tarefa
        manager.setMaxProgress(100);
        for (int i = 0; i < 100; i++) {
            manager.notifyProgress(i);
            try {
                Thread.sleep(50); // Simula o trabalho
            } catch (InterruptedException e) {
                manager.stop();
            }
        }
        return 100; // Retorna o resultado da tarefa
    }
});


Definindo Ações Antes e Depois
Você pode definir ações a serem realizadas antes e depois da tarefa utilizando os métodos before() e after(). O método before permite o pré-processamento dos parâmetros, e o método after manipula o pós-processamento dos resultados ou exceções.

worker.before(params -> {
    // Pré-processamento antes da tarefa
    return params;
}).after((result, exception) -> {
    // Pós-processamento após a tarefa
    if (exception == null) {
        // Manipula sucesso
        System.out.println("Tarefa concluída com sucesso com o resultado: " + result);
    } else {
        // Manipula falha
        System.err.println("Tarefa falhou com exceção: " + exception.getMessage());
    }
});


Executando a Tarefa
Para executar a tarefa, chame o método execute() e passe os parâmetros necessários. O método execute() retorna um objeto Feedback que você pode usar para monitorar o progresso e o status da tarefa.

Feedback<Integer> feedback = worker.execute("Parâmetro da Tarefa");


Monitorando o Progresso e o Status da Tarefa
Você pode monitorar o progresso e o status da tarefa utilizando a classe Feedback. Defina manipuladores de sucesso, progresso e falha para lidar com diferentes estados da tarefa.

if (feedback != null) {
    feedback.success((result, stopped) -> {
        // Manipula sucesso
        System.out.println("Tarefa sucedida com o resultado: " + result);
    }).progress((current, max) -> {
        // Manipula progresso
        System.out.println("Progresso da tarefa: " + current + "/" + max);
    }).failure(exception -> {
        // Manipula falha
        System.err.println("Tarefa falhou com exceção: " + exception.getMessage());
    });
}



Licença
Esta biblioteca está licenciada sob a Licença MIT. Veja o arquivo LICENSE para mais informações.

Contribuições
Contribuições são bem-vindas! Por favor, abra uma issue ou envie um pull request.

Contato
Para qualquer dúvida ou problema, entre em contato com Carlos Eduardo.

Este arquivo `README.md` em português fornece uma visão completa de como utilizar a classe `Worker`, com exemplos de uso e explicações sobre cada funcionalidade. Sinta-se à vontade para personalizar as partes que necessitarem de mais detalhes ou ajustes específicos.
