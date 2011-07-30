#!/bin/bash

tag=$1
mkdir -p target
cd target
  if [ ! -d checkout ]; then
    git clone git@github.com:twuni/treasury-server.git checkout
  else
    cd checkout
      git fetch
    cd ..
  fi
  cd checkout
    if [ -n "$tag" ]; then
      git checkout tags/$tag
    fi
    mvn clean package
  cd ..
  rm -fR quickstart
  mkdir quickstart
  mv checkout/target/*-with-dependencies.jar quickstart/a.zip
  cd quickstart
    unzip a.zip
    rm a.zip
    cp ../../treasury.properties.example ./treasury.properties
    if [ -n "$tag" ]; then
      tar -cf treasury-server-$tag-quickstart.tar *
    else
      tar -cf treasury-server-master-quickstart.tar *
    fi
    gzip *.tar
    mv *.tar.gz ../
  cd ..
  rm -fR quickstart
cd ..
