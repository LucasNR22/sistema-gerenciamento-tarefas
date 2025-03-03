#!/bin/bash

echo "Compilando Serviço de Usuários..."
cd usuario-service
./mvnw clean package -DskipTests
cd ..

echo "Compilando Serviço de Tarefas..."
cd tarefa-service
./mvnw clean package -DskipTests
cd ..

echo "Compilação concluída!"