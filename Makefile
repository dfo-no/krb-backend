IMAGE := ghcr.io/dfo-no/krb-backend
COMMIT := $$(git rev-parse --short HEAD)

version ?= $(COMMIT)

build:
	@mvn -B --no-transfer-progress clean package -DskipTests

test:
	@mvn -B --no-transfer-progress clean test

user_jar:
	@mvn -B --no-transfer-progress clean install -Dquarkus.package.type=uber-jar -DskipTests

native:
	@mvn -B --no-transfer-progress clean package -DskipTests -Pnative

native_docker:
	@mvn -B --no-transfer-progress clean package -DskipTests -Pnative -Dquarkus.native.container-build=true

docker:
	@docker build \
		-f src/main/docker/Dockerfile.jvm \
		-t $(IMAGE):$(COMMIT) \
		.

docker-push:
	@docker push $(IMAGE):$(version)

docker-tag:
	@docker tag $(IMAGE):$(COMMIT) $(IMAGE):$(version)

docker-run:
	@docker run --rm -it -p 8080:8080 $(IMAGE):$(version)