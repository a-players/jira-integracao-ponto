echo "removing previous build"
echo "building frontend resourdes"
source ~/.nvm/nvm.sh
cd ./frontend
rm -fv ./dist
npm install
npm run build

echo "deleting old web bundle resourses on linx-app app"

rm -fv ../backend/src/main/resources/css/*.css
rm -fv ../backend/src/main/resources/js/*.js

echo " "
echo "deleting done. "
echo " "

echo "copying/synchronizing current web bundle resourses to linx-app app"

cp ./dist/*.css ../backend/src/main/resources/css
cp ./dist/*.js ../backend/src/main/resources/js

echo " "
echo "copy/sync done. "
echo " "


echo "building package"
cd ../backend
atlas-mvn clean package
cp ./target/integracaoponto-1.0.0.jar ./dist/integracaoponto-1.0.0.jar