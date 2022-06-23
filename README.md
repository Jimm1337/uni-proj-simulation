# uni-proj-simulation
[![GitHub license](https://img.shields.io/github/license/Jimm1337/uni-proj-simulation?color=blue)](https://github.com/Jimm1337/uni-proj-simulation/blob/master/LICENSE)

Simple merchant simulation for uni.

## Build and Run
Tested on:
- OS: Arch 5.18.5-zen1-1-zen
- JDK: openjdk-18
- Gradle: 7.4.2

```shell
$ ./gradlew assembleDist
$ cd build/distributions
$ tar -xvf uni-proj-simulation-1.0.tar
$ cd uni-proj-simulation/bin
$ ./uni-proj-simulation <RNG> <StealMulti>
```

## Features
- Json
- Save state
- Console IO

## Documentation
- [Class schema](doc/class-schema.pdf)

#### Javadoc 
```shell
$ ./gradlew javadoc
```
Location: ./build/docs/javadoc/index.html

### todo
- Update schema
- Natural language doc (latex)
