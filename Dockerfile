FROM mcr.microsoft.com/java/jre:11u2-zulu-alpine
ARG version=0.0.3
MAINTAINER liangyongrui <awsd1235@163.com>
ADD https://github.com/easy-nas/easy-nas-server/releases/download/${version}/easy-nas-server-${version}.jar /usr/share/easy-nas/
CMD ["java", "-jar", "/usr/share/easy-nas/easy-nas-server-${version}.jar", "--spring.profiles.active=production"]