#-*- mode:conf; -*-

FROM postgres:9.4

RUN localedef -i sv_SE -c -f UTF-8 -A /usr/share/locale/locale.alias sv_SE.UTF-8

EXPOSE 5432

ENV LANG sv_SE.utf8
ENV PGDATA /var/lib/postgresql/data/pgdata
