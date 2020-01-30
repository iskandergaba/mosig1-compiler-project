.PHONY: all
all:
	@./gradlew compileJava

.PHONY: test
test: all
	@./testall

.PHONY: clean
clean:
	@./gradlew clean
