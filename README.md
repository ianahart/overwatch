# OverWatch

![React](https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=white&style=for-the-badge)
![TailwindCSS](https://img.shields.io/badge/TailwindCSS-38B2AC?logo=tailwindcss&logoColor=white&style=for-the-badge)
![Redux](https://img.shields.io/badge/Redux-764ABC?logo=redux&logoColor=white&style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=spring-boot&logoColor=white&style=for-the-badge)
![Java](https://img.shields.io/badge/Java-007396?logo=java&logoColor=white&style=for-the-badge)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black&style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white&style=for-the-badge)

## Built With

- React
- Tailwind CSS
- Redux
- Spring Boot
- PostgresSQL

## Description

[Overwatch](https://codeoverwatch.com)

Overwatch is a cutting-edge platform connecting users and reviewers for seamless code review experiences. Reviewers can earn through Stripe by reviewing code, managing tasks in Trello-like workspaces, and viewing code directly in the browser. Teams enable collaborative group messaging for enhanced communication. Users can connect with reviewers, request detailed code reviews, and chat directly. Overwatch offers robust features, including notification management, multi-factor authentication, and email alerts for key actions, ensuring a secure and streamlined experience for all.

## Table of Contents

- [OverWatch](#overwatch)
  - [Built With](#built-with)
  - [Description](#description)
  - [Table of Contents](#table-of-contents)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Visuals](#visuals)
  - [Tests](#tests)
  - [Credits](#credits)
  - [References](#references)
  - [License](#license)
  - [Contributing](#contributing)
  - [Badges](#badges)
  - [Questions](#questions)

## Installation

- `git clone https://github.com/ianahart/overwatch.git`
- **cd** into the root of the cloned project
- If you have not downloaded [postgresql](https://www.postgresql.org/) go ahead and do so.
- In the terminal run `psql postgres` and create a database called `overwatch` and `\c overwatch`
- in the root of the project, run `mvn clean install`
- now cd into the folder labeled **frontend** and run `npm install`
- inside the **frontend** directory run `npm run dev`
- inside the root of the project run `mvn spring-boot:run`

## Usage

More to come.

## Visuals

<img width="700" alt="overwatch_screenshot" src="https://github.com/ianahart/overwatch/assets/29121238/d036be82-0f5d-45e1-b78d-548c11d3fb10">
<img width="700" alt="explore-page" src="https://github.com/user-attachments/assets/43c900a8-a887-4cc8-88c9-66f921e5c7e0">
<img width="700" alt="explore-single-view" src="https://github.com/user-attachments/assets/b48cf039-ebae-4942-9bba-f39f2998ced4">
<img width="700" alt="main-trello" src="https://github.com/user-attachments/assets/f433530c-4672-4ddc-83e7-3bf68dafc28b">
<img width="700" alt="review" src="https://github.com/user-attachments/assets/1c9e3fa9-04d4-4fab-a4f8-ded90958c9ac">
<img width="700" alt="trello-options" src="https://github.com/user-attachments/assets/a1c764e6-acbd-4d14-a690-5f6f275b12f6">


## Tests

As of right now only the Spring Boot API has testing implemented. The frontend in React does not.

### Instructions to run the API testing.

- If you have not downloaded [postgresql](https://www.postgresql.org/) go ahead and do so.
- In the terminal run `psql postgres` and create a database called `overwatch_test`
- in the root of the project, run `mvn clean install`
- to run all tests use `mvn test`
- To run a test against an individual file, use `mvn test -Dtest="<nameOfFile>"`

## Credits

N/A

## References

## License

This project is covered under MIT License

<details>
  <summary>
    License Text
  </summary>

```

Copyright (c) 2024  Ian Hart

Permission is hereby granted, free of charge, to any perOAson obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```

</details>

## Contributing

No contributions are being accepted at this time.

## Badges

[![GitHub license](https://img.shields.io/github/license/ianahart/overwatch)](https://github.com/ianahart/overwatch/blob/main/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/ianahart/overwatch)](https://github.com/ianahart/overwatch/issues)
[![GitHub stars](https://img.shields.io/github/stars/ianahart/overwatch)](https://github.com/ianahart/overwatch/stargazers)

## Questions

- Get in touch with me through [email](mailto:ianalexhart@gmail.com).
- Check out my GitHub [profile](https://github.com/ianahart).
