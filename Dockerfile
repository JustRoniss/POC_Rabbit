FROM ubuntu:latest
LABEL authors="ronis"

ENTRYPOINT ["top", "-b"]