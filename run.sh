#!/bin/bash

# build chaos monkey image
mvn clean package
eval $(minikube docker-env)
docker build ./ --tag chaos-monkey:miniKubeTag

# create testing environment
kubectl create namespace testing
kubectl apply -f ./testing_namespace/nginx_red_label.yaml
kubectl apply -f ./testing_namespace/nginx_white_label.yaml

# prepare service account for chaos-monkey
kubectl create namespace chaos-monkey
kubectl apply -f ./k8s/role.yaml
kubectl apply -f ./k8s/roleBinding.yaml
kubectl apply -f ./k8s/serviceAccount.yaml

# add configuration for chaos-monkey
kubectl apply -f ./k8s/configMap.yaml

# deploy chaos monkey
kubectl apply -f ./k8s/pod.yaml

kubectl wait --namespace=chaos-monkey --for=condition=Ready pod/chaos-monkey-pod

# port-forward locally the application
kubectl --namespace=chaos-monkey port-forward $(kubectl get pod --namespace=chaos-monkey -l app=chaos-monkey -o jsonpath="{.items[0].metadata.name}") 8080