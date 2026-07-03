FROM node:22-alpine AS build
WORKDIR /app
COPY webApp/package*.json ./
RUN npm ci
COPY webApp/ ./
RUN npm run build

FROM nginx:1.27-alpine
COPY webApp/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
