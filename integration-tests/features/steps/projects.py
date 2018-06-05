import requests

from behave import given, then, when


@when('I read project list')
def read_project_list(context):
    """Access the service API using the HTTP GET method and read project list."""
    url = "/project-list"
    context.response = requests.get(context.api_url + url)


@when('I read projects')
def read_project_list(context):
    """Access the service API using the HTTP GET method and read projects."""
    url = "/projects"
    context.response = requests.get(context.api_url + url)


@then('I should find {num:d} projects')
def check_key_in_json(context, num):
    """Check the project list returned by the REST API."""
    data = context.response.json()
    assert data is not None
    projects = len(data)
    assert projects == num, "Improper number of projects found: {f}, but {n} expected".format(f=projects, n=num)


@then('I should find project with ID={id:d}, AOID={aoid}, and name={name}')
def check_key_in_json(context, id, aoid, name):
    """Check the project list returned by the REST API."""
    data = context.response.json()
    assert data is not None

    found = False

    for project in data:
        assert "id" in project
        assert "aoid" in project
        assert "name" in project
        assert "created" in project

        if project["id"] == id and project["aoid"] == aoid and project["name"] == name:
            found = True
            break

    assert found, "The project can not be found in the response"
