Write-Output "removing previous build"
Write-Output "building frontend resourdes"

$env:NODE_OPTIONS = "--openssl-legacy-provider"
Set-Location ./frontend
if (Test-Path ./dist) {

    Remove-Item -Force -Recurse  ./dist
    }
npm install
npm run build

Write-Output "deleting old web bundle resourses on linx-app app"

Remove-Item -Force -Recurse ../backend/src/main/resources/css/*.css
Remove-Item -Force -Recurse ../backend/src/main/resources/js/*.js

Write-Output " "
Write-Output "deleting done. "
Write-Output " "

Write-Output "copying/synchronizing current web bundle resourses to linx-app app"

Copy-Item ./dist/*.css ../backend/src/main/resources/css
Copy-Item ./dist/*.js ../backend/src/main/resources/js

Write-Output " "
Write-Output "copy/sync done. "
Write-Output " "


Write-Output "building package"

Set-Location ../backend
atlas-mvn clean package

Copy-Item ./target/integracaoponto-1.0.0.jar ./dist/integracaoponto-1.0.0.jar