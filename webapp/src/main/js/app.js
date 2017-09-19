'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const BS = require('react-bootstrap')

class App extends React.Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        const title = (
            <h3>Twitter Fight!</h3>
        );
        return (
            <div className="container">
                <BS.Panel header={title} bsStyle="success">
                    <BS.Grid>
                        <BS.Row className="show-grid">
                            <BS.Col xs={6}>Contender 1</BS.Col>
                            <BS.Col xs={6}>Contender 2</BS.Col>
                        </BS.Row>
                    </BS.Grid>
                </BS.Panel>
            </div>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)