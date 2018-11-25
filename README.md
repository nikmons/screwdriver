[![Build Status](https://travis-ci.com/NickMns/screwdriver.svg?branch=master)](https://travis-ci.com/NickMns/screwdriver)

# screwdriver
Manage/Monitor IT hardware servicing lifecycle

The repository contains a total of 3 projects.

1. **api** - A python REST API (flask)
2. **client** - Client Application (Android)
3. **Tracker** - Helper Application (Js)

# API

The API is responsible for handling user authentication/authorization
(requested by the client app) and resource serving.

## database

Persistent data storage is handled by **postgresql**.\
**SQLAlchemy** ORM is used for model/data handling and communication with the db.\
Database migrations are performed using **alembic**,

## pipenv

This project uses *pipenv* for virtual environment management.
To get started with pipenv you need to install it via pip.

```shell
cd api
pip install pipenv
```

Then following command initializes a new virtual environemnt,
that will be used for the current project,

```shell
pipenv install
```

To use the virtual environment we can either start a new shell
inside it.

```shell
pipenv shell
```

Or run single commands by using the run argument

```shell
pipenv run python --version
```

As pipenv also offers package manager capabilities we no londer need to
use pip for package installation/removal.\

```shell
pipenv install pylint
```

To update *Pipfile.lock* we can use the following command.

```shell
pipenv lock
```

## flask

To start a local development server of flask use the following command (always in the virtual environment)

```shell
pipenv run flask run
```