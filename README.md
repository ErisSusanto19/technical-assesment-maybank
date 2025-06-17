# Technical Assessment: End-to-End CI/CD Backend Service

Proyek ini adalah implementasi dari sebuah alur kerja CI/CD (Continuous Integration/Continuous Deployment) untuk aplikasi backend sederhana yang dibangun dengan Java Spring Boot.

Tujuan proyek ini adalah untuk mendemonstrasikan kemampuan dalam kontainerisasi aplikasi menggunakan Docker, orkestrasi dengan Kubernetes, dan otomasi proses build menggunakan Jenkins.

## Desain Alur Kerja CI/CD

Alur kerja dari perubahan kode hingga aplikasi berjalan di Kubernetes dijelaskan dalam langkah-langkah berikut:

#### Bagian 1: Continuous Integration (CI) - Otomatis oleh Jenkins
Proses ini dimulai saat pipeline di Jenkins dijalankan (bisa dipicu secara manual atau otomatis oleh `git push`).

1.  **Checkout Kode**: Jenkins mengambil source code terbaru dari branch `main` di repository GitHub ini.

2.  **Build & Test Aplikasi**: Aplikasi di-build dan di-test menggunakan Maven (`mvn clean package`). Proses ini dijalankan di dalam sebuah container Docker `maven:3.9-eclipse-temurin-21` yang terisolasi untuk memastikan lingkungan build selalu bersih dan konsisten.

3.  **Build Docker Image**: Setelah aplikasi berhasil di-build menjadi file `.jar`, Jenkins akan menggunakan `Dockerfile` yang ada di repository ini untuk membangun sebuah Docker image baru. Image ini diberi tag unik berdasarkan nomor build Jenkins (contoh: `simple-backend:build-1`, `simple-backend:build-2`, dst).

Pada titik ini, proses CI selesai. Hasilnya adalah sebuah artifact (Docker image) baru yang siap untuk di-deploy.

#### Bagian 2: Continuous Deployment (CD) - Manual oleh Developer
Untuk lingkungan development lokal ini, proses deployment dilakukan secara manual setelah CI berhasil.

4.  **Muat Image ke Minikube**: Developer memuat image yang baru dibuat oleh Jenkins ke dalam environment Docker internal milik Minikube menggunakan perintah:
    ```
    minikube image load simple-project:build-X
    ```
5.  **Update Aplikasi di Kubernetes**: Developer kemudian memerintahkan Kubernetes untuk mengupdate aplikasi yang sedang berjalan ke versi image yang baru dengan perintah:
    ```
    kubectl set image deployment/spring-boot-app-deployment spring-boot-container=simple-project:build-X
    ```
    Kubernetes akan secara otomatis melakukan proses *rolling update* untuk memastikan tidak ada downtime.

6.  **Selesai**: Aplikasi dengan versi terbaru kini berjalan di cluster dan dapat diakses melalui Ingress.

## Teknologi yang Digunakan
- **Backend**: Java 21, Spring Boot 3.x, Maven
- **Kontainerisasi**: Docker
- **Orkestrasi**: Kubernetes (dijalankan secara lokal dengan Minikube)
- **CI/CD Server**: Jenkins (dijalankan sebagai kontainer Docker kustom)

## Prasyarat
- Java 21
- Maven
- Docker
- Minikube
- kubectl

## Struktur Proyek
- `src/`: Source code aplikasi Spring Boot.
- `pom.xml`: Konfigurasi dependensi Maven.
- `Dockerfile`: Resep untuk membangun image aplikasi Spring Boot (multi-stage build).
- `.dockerignore`: Daftar file/folder yang diabaikan oleh Docker saat build.
- `deployment.yaml`: Manifest Kubernetes untuk mendefinisikan bagaimana aplikasi di-deploy.
- `service.yaml`: Manifest Kubernetes untuk mengekspos aplikasi di dalam cluster.
- `ingress.yaml`: Manifest Kubernetes untuk mengekspos aplikasi ke luar cluster dengan hostname.
- `Jenkinsfile`: Skrip Pipeline as Code yang berisi definisi alur kerja CI.

## Langkah-langkah Setup & Deployment

Proses ini dibagi menjadi dua bagian: setup lingkungan satu kali dan alur kerja CI/CD harian.

### Bagian 1: Setup Lingkungan (Satu Kali)

1.  **Build Image Jenkins Kustom:**
    Buat `Dockerfile` terpisah untuk Jenkins yang menginstall Docker CLI, lalu build:
    ```bash
    # Dari dalam folder setup Jenkins
    docker build -t my-custom-jenkins:v1 .
    ```

2.  **Jalankan Container Jenkins:**
    ```bash
    docker run -d -p 8080:8080 -p 50000:50000 --name jenkins \
    -v jenkins_home:/var/jenkins_home \
    -v /var/run/docker.sock:/var/run/docker.sock \
    my-custom-jenkins:v1
    ```

3.  **Jalankan Cluster Kubernetes:**
    ```bash
    minikube start --driver=docker
    minikube addons enable ingress
    ```

4.  **Konfigurasi DNS Lokal:**
    Tambahkan IP Minikube ke file `/etc/hosts` Anda.
    ```bash
    # Dapatkan IP
    minikube ip

    # Edit file hosts (ganti IP_MINIKUBE dengan IP di atas)
    # sudo nano /etc/hosts
    # --> Tambahkan baris: IP_MINIKUBE simple-project.test
    ```

5.  **Setup Job di Jenkins UI:**
    - Lakukan setup awal Jenkins di `http://localhost:8080`.
    - Install plugin `Docker Pipeline`.
    - Buat "Pipeline job" baru yang menunjuk ke repository Git ini.

### Bagian 2: Alur Kerja CI/CD
(Lihat bagian "Desain Alur Kerja CI/CD" di atas).

## Cara Mengakses Aplikasi
Setelah proses deployment selesai, aplikasi dapat diakses melalui browser di:
[http://simple-project.test/](http://simple-project.test/)