import { configure, shallow, mount} from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import SignIn from 'pages/Login/index'
import Register from 'pages/Register/index'
import LandingPage from 'pages/LandingPage/index'
import PageNotFound from 'pages/PageNotFound/index'
import Gallery from 'pages/Gallery/index'
import ChatBox from 'pages/Support/ChatBox'
import ChatBoxes from 'pages/Support/index'
import Users from 'pages/Users/index'
import UserPage from 'pages/UserPage/index'
import DealCard from 'pages/UserPage/DealCard'
import JoBCard from 'pages/Worker/JobCard'
import WorkerPage from 'pages/Worker/index'

configure({adapter: new Adapter()});
it("Login page rendering without crashing", () => {
  shallow(<SignIn />);
});

test("Login page should have username and password field ", () => {
  const component = shallow(<SignIn />);
  expect(component.find('#password')).toHaveLength(1);
  expect(component.find('#username')).toHaveLength(1);

});

it("Register page rendering without crashing", () => {
  shallow(<Register />);
});

test("Register page should have usernamem password, lastname, firstname, number, city and email fields ", () => {
  const component = shallow(<Register />);
  expect(component.find('#password')).toHaveLength(1);
  expect(component.find('#username')).toHaveLength(1);
  expect(component.find('#lastName')).toHaveLength(1);
  expect(component.find('#firstName')).toHaveLength(1);
  expect(component.find('#number')).toHaveLength(1);
  expect(component.find('#city')).toHaveLength(1);
  expect(component.find('#email')).toHaveLength(1);

});

it("Landing page rendering without crashing", () => {
  shallow(<LandingPage />);
});

it("Page not found rendering without crashing", () => {
  shallow(<PageNotFound />);
});

test("Page not found rendering without crashing ", () => {
  const component = shallow(<PageNotFound />);
  expect(component.find('.not-found-container')).toHaveLength(1);

});

it("Gallery component rendering without crashing", () => {
  shallow(<Gallery />);
});

test("Gallery shoud have modal for showing images", () => {
  const component = shallow(<Gallery/>);
  expect(component.find('.imageModal')).toHaveLength(1);
});

it("ChatBox rendering without crashing", () => {
  shallow(<ChatBox />);
});

it("Support page rendering without crashing", () => {
  shallow(<ChatBoxes />);
});

test("Support page should have grid for support messages", () => {
  const component = shallow(<ChatBoxes />);
  expect(component.find('.messageGrid')).toHaveLength(1);
});

it("Users rendering without crashing", () => {
  shallow(<Users />);
});

it("Users page should have all textfields ", () => {
  const component = shallow(<Users />);
  expect(component.find('#username')).toHaveLength(1);
  expect(component.find('#lastName')).toHaveLength(1);
  expect(component.find('#firstName')).toHaveLength(1);
  expect(component.find('#number')).toHaveLength(1);
  expect(component.find('#city')).toHaveLength(1);
  expect(component.find('#email')).toHaveLength(1);
});

it("Deal Card rendering without crashing", () => {

  const deal = {
        id: '6rPO02ozF3bM7NnOV4h6s2',
        userName: 'ime',
        artist: 'sgs',
        job: {
          business: {
              name: 'ime biznisa'
            },
          name: 'ime posla',
          price: 13,
          priceType: 'PER_HOUR'
        }
  }

  shallow(<DealCard deal={deal} />);
});

it("Deal Card rendering without crashing", () => {

  const deal = {
        id: '6rPO02ozF3bM7NnOV4h6s2',
        userName: 'ime',
        artist: 'sgs',
        job: {
          business: {
              name: 'ime biznisa'
            },
          name: 'ime posla',
          price: 13,
          priceType: 'PER_HOUR'
        }
  }

  const component = shallow(<DealCard deal={deal} />);
  expect(component.find('.dealcardBox')).toHaveLength(2);
  expect(component.find('.dealcardBox').first().text().includes('ime biznisa'))
});

it("Deal Card rendering without crashing", () => {

    const deal = {
          id: '6rPO02ozF3bM7NnOV4h6s2',
          userName: 'ime',
          artist: 'sgs',
          job: {
            business: {
                name: 'ime biznisa'
              },
            name: 'ime posla',
            price: 13,
            priceType: 'PER_HOUR'
          }
    }

    shallow(<DealCard deal={deal} />);
});

it("Deal Card rendering its fields", () => {

    const deal = {
          id: '6rPO02ozF3bM7NnOV4h6s2',
          userName: 'ime',
          artist: 'sgs',
          job: {
            business: {
                name: 'ime biznisa'
              },
            name: 'ime posla',
            price: 13,
            priceType: 'PER_HOUR'
          }
    }

    const component = shallow(<DealCard deal={deal} />);
    expect(component.find('.dealcardBox')).toHaveLength(2);
    expect(component.find('.dealcardBox').first().text().includes('ime biznisa'))
});

it("User Page rendering without crashing", () => {

     const user = {
          id: '6rPO02ozF3bM7NnOV4h6s2',
          firstName: 'ime',
          lastName: 'prezime',
          email: 'mail',
          number: '062731456',
          username : 'juzernejm',
          city: 'Sarajevo'
    }

  const sessionItem = 'emajstor-session'
  const session = localStorage.getItem(sessionItem)
  localStorage.setItem(
    sessionItem,
    JSON.stringify({
      ...JSON.parse(session),
      ...user,
    })
  )


    shallow(<UserPage />);
});

it("User Page should have all fields", () => {

  const user = {
       id: '6rPO02ozF3bM7NnOV4h6s2',
       firstName: 'ime',
       lastName: 'prezime',
       email: 'mail',
       number: '062731456',
       username : 'juzernejm',
       city: 'Sarajevo'
 }

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
 sessionItem,
 JSON.stringify({
   ...JSON.parse(session),
   ...user,
 })
)

const component = shallow(<UserPage />);
    expect(component.find('#username')).toHaveLength(1)
    expect(component.find('#firstName')).toHaveLength(1)
    expect(component.find('#lastName')).toHaveLength(1)
    expect(component.find('#email')).toHaveLength(1)
    expect(component.find('#number')).toHaveLength(1)
    expect(component.find('#city')).toHaveLength(1)
})

it("Search bar rendering with its fields", () => {
  const user = {
    id: '6rPO02ozF3bM7NnOV4h6s2',
    firstName: 'ime',
    lastName: 'prezime',
    email: 'mail',
    number: '062731456',
    username : 'juzernejm',
    city: 'Sarajevo'
}

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
sessionItem,
JSON.stringify({
...JSON.parse(session),
...user,
})
)

const component = shallow(<UserPage />);
expect(component.find('.userSearchBar')).toHaveLength(1);

});

it("User page images and reviews rendering", () => {
  const user = {
    id: '6rPO02ozF3bM7NnOV4h6s2',
    firstName: 'ime',
    lastName: 'prezime',
    email: 'mail',
    number: '062731456',
    username : 'juzernejm',
    city: 'Sarajevo'
}

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
sessionItem,
JSON.stringify({
...JSON.parse(session),
...user,
})
)

const component = shallow(<UserPage />);
expect(component.find('.userReview')).toHaveLength(1);
expect(component.find('.userImages')).toHaveLength(1);

});

it("User page renders with user jobs", () => {
  const user = {
    id: '6rPO02ozF3bM7NnOV4h6s2',
    firstName: 'ime',
    lastName: 'prezime',
    email: 'mail',
    number: '062731456',
    username : 'juzernejm',
    city: 'Sarajevo'
}

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
sessionItem,
JSON.stringify({
...JSON.parse(session),
...user,
})
)

const component = shallow(<UserPage/>);
expect(component.find('.userJobs')).toHaveLength(1);
});

it("jOBCard rendering its fields", () => {

  const deal = {
        id: '6rPO02ozF3bM7NnOV4h6s2',
        userName: 'ime',
        artist: 'sgs',
        job: {
          business: {
              name: 'ime biznisa'
            },
          name: 'ime posla',
          price: 13,
          priceType: 'PER_HOUR'
        }
  }

  const component = shallow(<JoBCard deal={deal} />);
  expect(component.find('.workerDeal')).toHaveLength(1);
  expect(component.find('.workerDeal').first().text().includes('ime biznisa'))
  expect(component.find('.workerDeal').first().text().includes('ime posla'))
});

it("Worker Page rendering without crashing", () => {

  const user = {
       id: '6rPO02ozF3bM7NnOV4h6s2',
       firstName: 'ime',
       lastName: 'prezime',
       email: 'mail',
       number: '062731456',
       username : 'juzernejm',
       city: 'Sarajevo'
 }

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
 sessionItem,
 JSON.stringify({
   ...JSON.parse(session),
   ...user,
 })
)


 shallow(<WorkerPage />);
});

it("Worker Page profile should have all fields", () => {

  const user = {
       id: '6rPO02ozF3bM7NnOV4h6s2',
       firstName: 'ime',
       lastName: 'prezime',
       email: 'mail',
       number: '062731456',
       username : 'juzernejm',
       city: 'Sarajevo'
 }

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
 sessionItem,
 JSON.stringify({
   ...JSON.parse(session),
   ...user,
 })
)

const component = shallow(<WorkerPage />);
    expect(component.find('#username')).toHaveLength(1)
    expect(component.find('#firstName')).toHaveLength(1)
    expect(component.find('#lastName')).toHaveLength(1)
    expect(component.find('#email')).toHaveLength(1)
    expect(component.find('#number')).toHaveLength(1)
    expect(component.find('#city')).toHaveLength(1)
})

it("Worker Page profile should have adding new job form", () => {

  const user = {
       id: '6rPO02ozF3bM7NnOV4h6s2',
       firstName: 'ime',
       lastName: 'prezime',
       email: 'mail',
       number: '062731456',
       username : 'juzernejm',
       city: 'Sarajevo'
 }

const sessionItem = 'emajstor-session'
const session = localStorage.getItem(sessionItem)
localStorage.setItem(
 sessionItem,
 JSON.stringify({
   ...JSON.parse(session),
   ...user,
 })
)

const component = shallow(<WorkerPage />);
    expect(component.find('.workerNewJob')).toHaveLength(1);
    expect(component.find('#workerBusinessname')).toHaveLength(1)
    expect(component.find('#workerPrice')).toHaveLength(1)
    expect(component.find('#workerPricetype')).toHaveLength(1)
})
