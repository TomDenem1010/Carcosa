kubectl apply -f=carcosa.yaml

kubectl get deployments
kubectl get services
kubectl get pods
kubectl get pods -l app=carcosa -o jsonpath="{range .items[*]}{.metadata.name}{'\t'}{.spec.containers[0].image}{'\n'}{end}"

kubectl logs -f carcosa-7f776d7876-g8ftl
