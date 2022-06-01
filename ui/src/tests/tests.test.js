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

configure({adapter: new Adapter()});

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

    shallow(<UserPage  {...user}/>);
});
