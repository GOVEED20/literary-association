import React from 'react'
import { Nav } from 'react-bootstrap'
import { LinkContainer } from 'react-router-bootstrap'

const NavbarLink = ({ text, url }) => (
    <LinkContainer to={url}>
        <Nav.Link>
            {text}
        </Nav.Link>
    </LinkContainer>
)

export default NavbarLink