#!/bin/bash

# clean target
mvn clean

# delete images
eval $(minikube docker-env)
docker rmi $(docker images -a -q)
# clean k8s
kubectl delete namespace testing
kubectl delete namespace chaos-monkey

