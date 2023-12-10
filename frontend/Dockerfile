FROM node:18-alpine AS build
WORKDIR /home/app
COPY ./package*.json ./
RUN --mount=type=cache,target=/usr/src/app/.npm \
    npm set cache /usr/src/app/.npm && \
    npm ci
ARG BACKEND_URL
ENV VITE_BACKEND_URL $BACKEND_URL
COPY . .
RUN npm run build

FROM build as test
CMD ["npm", "test"]

FROM nginx as production
COPY --from=build /home/app/nginx.conf /etc/nginx/conf.d/default.conf
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build /home/app/dist .
ENTRYPOINT ["nginx", "-g", "daemon off;"]


