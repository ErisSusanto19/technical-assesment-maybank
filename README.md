# Technical Assessment - Backend Service

Proyek ini adalah implementasi backend service sederhana menggunakan Java Spring Boot. Aplikasi ini dikemas menggunakan Docker dan dideploy ke Kubernetes.

## Teknologi yang Digunakan
- Java 21
- Spring Boot 3.x
- Maven
- Docker
- Kubernetes (Minikube)
- Jenkins (Desain Konseptual)

## Cara Menjalankan

### 1. Menjalankan Secara Lokal (Tanpa Docker)
```bash
# Pastikan Anda memiliki Java 21 & Maven
./mvnw spring-boot:run
```
Aplikasi akan berjalan di `http://localhost:8080`.

### 2. Menjalankan dengan Docker & Kubernetes (Lokal)
```bash
# 1. Build Docker image
docker build -t simple-project:v1 .

# 2. Load image ke Minikube
minikube image load simple-project:v1

# 3. Deploy ke Kubernetes
kubectl apply -f deployment.yaml -f service.yaml -f ingress.yaml

# 4. Dapatkan IP Minikube dan edit /etc/hosts
# minikube ip -> tambahkan ke /etc/hosts dengan domain simple-project.test

# 5. Akses aplikasi melalui Ingress
# [http://simple-project.test/](http://simple-project.test/)
```