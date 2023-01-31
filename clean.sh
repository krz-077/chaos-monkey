#!/bin/bash

# clean target
mvn clean

# clean k8s
kubectl delete namespace testing
kubectl delete namespace chaos-monkey

# delete images
eval $(minikube docker-env)
docker rmi $(docker images -a -q)
