FROM node:16 AS build-stage
LABEL maintainer="abdeenahmed2002@hotmail.com"
WORKDIR /app
COPY . .
RUN npm install
RUN npm install --location=global @angular/cli
RUN ng build

FROM nginx:alpine as run-stage
RUN mkdir -p /var/www/mfb/html
COPY conf/nginx-mfb.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /app/dist/mfb-frontend /var/www/mfb/html
EXPOSE 8383
CMD sed -i "s/http\:\/\/localhost\:8387/http\:\/\/$MFB_REST_URI/g" /var/www/mfb/html/main.*.js && nginx -g 'daemon off;'
