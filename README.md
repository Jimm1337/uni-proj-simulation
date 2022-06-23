# uni-proj-simulation
[![GitHub license](https://img.shields.io/github/license/Jimm1337/uni-proj-simulation?color=blue)](https://github.com/Jimm1337/uni-proj-simulation/blob/master/LICENSE)

Simple merchant simulation for uni.

## <ins>Build and Run
Tested on:
- OS: Arch 5.18.5-zen1-1-zen
- JDK: openjdk-18
- Gradle: 7.4.2

```shell
$ git clone https://github.com/Jimm1337/uni-proj-simulation.git
$ cd uni-proj-simulation
$ ./gradlew assembleDist
$ cd build/distributions
$ tar -xvf uni-proj-simulation-1.0.tar
$ cd uni-proj-simulation-1.0/bin
$ ./uni-proj-simulation <RNG> <StealMulti>
```

## <ins>Features
- Json
- Save state
- Console IO

## <ins>Documentation
- [Natural language PL](doc/natural-language-PL.pdf)
- [UML schemas by package](doc/schemas-png)
- [UML schema PDF](doc/schema-pdf.pdf)

### Javadoc 
```shell
$ ./gradlew javadoc
```
Location: ./build/docs/javadoc/index.html
