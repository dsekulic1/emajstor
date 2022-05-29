import React from 'react'
import styled from 'styled-components'
import { Link } from 'react-scroll'
// Assets
import { ReactComponent as LogoImg } from '../../images/logo.svg'

export default function Contact() {
  const getCurrentYear = () => {
    return new Date().getFullYear()
  }

  return (
    <Wrapper>
      <div className='darkBg'>
        <div className='container'>
          <InnerWrapper
            className='flexSpaceCenter'
            style={{ padding: '5px 0' }}
          >
            <Link
              className='flexCenter animate pointer'
              to='home'
              smooth={true}
              offset={-80}
            >
              <LogoImg />
            </Link>
            <StyleP className='whiteColor font14'>
              © {getCurrentYear()} -{' '}
              <span className='purpleColor font14'>eMajstor</span> All Right
              Reserved
            </StyleP>
          </InnerWrapper>
        </div>
      </div>
    </Wrapper>
  )
}

const Wrapper = styled.div`
  width: 100%;
`
const InnerWrapper = styled.div`
  @media (max-width: 550px) {
    flex-direction: column;
  }
`
const StyleP = styled.p`
  @media (max-width: 550px) {
    margin: 20px 0;
  }
`
