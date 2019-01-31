FROM mcr.microsoft.com/java/jre:11u2-zulu-alpine
MAINTAINER liangyongrui <awsd1235@163.com>
ADD https://github.com/easy-nas/easy-nas-server/releases/download/0.0.2/easy-nas-server-0.0.2.jar /usr/share/easy-nas/
CMD ["java", "-jar", "/usr/share/easy-nas/easy-nas-server-0.0.2.jar", "--spring.profiles.active=production"]