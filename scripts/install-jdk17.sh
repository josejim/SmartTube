#!/bin/bash
# Install JDK 17 for SmartTube compilation
# Run: chmod +x install-jdk17.sh && ./install-jdk17.sh

set -e

echo "=== Installing OpenJDK 17 ==="
sudo apt update
sudo apt install -y openjdk-17-jdk

echo "=== Verifying installation ==="
java -version
echo ""
echo "=== JAVA_HOME ==="
echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc

echo "=== Done! Run these to use JDK 17 ==="
echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
echo "cd /home/josejc/src/smartube-fork && ./gradlew :common:compileDebugJavaWithJavac"