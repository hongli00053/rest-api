FROM payara/micro:6.2023.12

# 复制 WAR 文件到 Payara Micro
COPY target/rest-acmemedical-0.0.1-SNAPSHOT.war $DEPLOY_DIR

# 设置环境变量
ENV PAYARA_MICRO_JVM_OPTS="-Xmx512m"

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["--port", "8080", "--contextroot", "/"]