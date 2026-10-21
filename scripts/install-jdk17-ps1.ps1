# SmartTube Build Environment Setup
# Run this from PowerShell (Windows) - NOT from WSL

Write-Host "=== Installing JDK 17 in WSL ===" -ForegroundColor Cyan

# Install JDK 17 via WSL
wsl -e bash -c "sudo apt update && sudo apt install -y openjdk-17-jdk"

# Verify
Write-Host "`n=== Verifying Java ===" -ForegroundColor Cyan
wsl -e bash -c "java -version"

Write-Host "`n=== DONE ===" -ForegroundColor Green
Write-Host "To compile SmartTube, run:"
Write-Host "  wsl -e bash -c 'cd /home/josejc/src/smartube-fork && export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 && ./gradlew :common:compileDebugJavaWithJavac'"