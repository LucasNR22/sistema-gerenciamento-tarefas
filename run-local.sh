#!/bin/bash

echo "Iniciando serviço de Usuários..."
cd usuario-service
./mvnw spring-boot:run &
cd ..

# Aguardar o serviço de usuários iniciar
sleep 15

echo "Iniciando serviço de Tarefas..."
cd tarefa-service
./mvnw spring-boot:run