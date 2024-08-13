# OverWatch

![React](https://camo.githubusercontent.com/e95e1cbdf8a6d197063c7e8765a79deb9b853081012d6e892adb6ac2c364397c/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d526561637426636f6c6f723d323232323232266c6f676f3d5265616374266c6f676f436f6c6f723d363144414642266c6162656c3d)
![Tailwind](https://camo.githubusercontent.com/5bc7d3c0a398ed32883cad4225d4f5a6718c95c014f86fe977b0cd110256a83e/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d5461696c77696e642b43535326636f6c6f723d323232323232266c6f676f3d5461696c77696e642b435353266c6f676f436f6c6f723d303642364434266c6162656c3d)
![Redux](https://camo.githubusercontent.com/5ad4694f1a8658b0f758fd7a396bb1e78126329e6cc9c44bb3300eb407212b9a/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d526564757826636f6c6f723d373634414243266c6f676f3d5265647578266c6f676f436f6c6f723d464646464646266c6162656c3d)
![Spring Boot](https://camo.githubusercontent.com/f4a35c8c4d475cbde11892b5fbb4735de63043c884ca4616f91fcc494200fa00/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d537072696e672b426f6f7426636f6c6f723d364442333346266c6f676f3d537072696e672b426f6f74266c6f676f436f6c6f723d464646464646266c6162656c3d)
![PostgresSQL](https://camo.githubusercontent.com/aaf7d409d95158427f9389c20305d66299f4e15d96bfa9d4f0792b21ad01e327/68747470733a2f2f696d672e736869656c64732e696f2f7374617469632f76313f7374796c653d666f722d7468652d6261646765266d6573736167653d506f737467726553514c26636f6c6f723d343136394531266c6f676f3d506f737467726553514c266c6f676f436f6c6f723d464646464646266c6162656c3d)

## Built With

- React
- Tailwind CSS
- Redux
- Spring Boot
- PostgresSQL

## Description

OverWatch is a marketplace for users to get code reviews from industry professionals. More of a description to come.

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
