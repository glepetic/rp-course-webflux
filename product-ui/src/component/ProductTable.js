import * as React from "react";
import {Component} from "react";
import './ProductTable.css';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TablePagination from '@mui/material/TablePagination';
import Paper from '@mui/material/Paper';
import TextField from "@mui/material/TextField";
import {Button} from "@mui/material";
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';


const DEFAULT_MAX = 1000
const INTEGER_MAX = Math.pow(2, 31) - 1

class ProductTable extends Component {

    constructor(props) {
        super(props);
        const streamSource = this.sourceStream(DEFAULT_MAX);
        this.state = {
            products: [],
            eventSource: streamSource,
            page: 0,
            rowsPerPage: 5,
            maxPriceInput: "",
            maxPrice: DEFAULT_MAX,
            validationMessage: "",
        }
        this.listen(streamSource);
    }

    sourceStream = value => new EventSource("http://localhost:8091/products/stream/" + value)

    listen = (streamSource) => streamSource.onmessage = evt => {
        const product = JSON.parse(evt.data);
        this.setState({
            products: [product, ...this.state.products],
        });
    }

    handleInputChange = evt => {
        const value = evt.target.value;
        const numValue = Number.parseInt(value);
        const errorMessage = value !== "" && (Number.isNaN(numValue) || numValue <= 0 || numValue > INTEGER_MAX) ?
            "Expected integer greater than 0." : "";
        this.setState({
            maxPriceInput: value,
            validationMessage: errorMessage,
        })
    }

    handleInputSubmit = () => {
        this.state.eventSource.close();
        const streamSource = this.sourceStream(this.state.maxPriceInput);
        this.setState({
            maxPriceInput: "",
            maxPrice: this.state.maxPriceInput,
            eventSource: streamSource,
            products: []
        });
        this.listen(streamSource);
    }

    handleChangePage = (event, newPage) =>
        this.setState({
                page: newPage,
            }
        )

    handleChangeRowsPerPage = event =>
        this.setState({
            page: 0,
            rowsPerPage: +event.target.value,
        })
    getProductRows = () => {
        if (this.state.products.length === 0) return (
            <TableCell colSpan={3}>Nothing has arrived yet...</TableCell>
        )
        return (
            this.state.products
                .slice(this.state.page * this.state.rowsPerPage, this.state.page * this.state.rowsPerPage + this.state.rowsPerPage)
                .map(product =>
                    <TableRow
                        key={product.id}
                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                    >
                        <TableCell component="th" scope="row">
                            {product.id}
                        </TableCell>
                        <TableCell align="right">{product.detail}</TableCell>
                        <TableCell align="right">{product.price}</TableCell>
                    </TableRow>
                )
        )
    };

    render() {
        return (
            <div>
                <div className="submitter">
                    <TextField onChange={this.handleInputChange}
                               value={this.state.maxPriceInput}
                               label="Price"
                               variant="standard"
                               autocomplete="off"
                               error={this.state.validationMessage !== ""}
                               helperText={this.state.validationMessage}
                               className="submitter-item"
                    />
                    <Button disabled={this.state.validationMessage !== "" || this.state.maxPriceInput === ""}
                            onClick={this.handleInputSubmit}
                            variant="contained"
                            endIcon={<NotificationsActiveIcon/>}
                            className="submitter-item"
                    >
                        Subscribe
                    </Button>
                </div>
                <div className="text-title">Product events with maximum price of {this.state.maxPrice}</div>
                <TableContainer component={Paper}>
                    <Table sx={{minWidth: 650}} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell align="right">Name</TableCell>
                                <TableCell align="right">Price</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {this.getProductRows()}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[3, 5, 10]}
                    component="div"
                    count={this.state.products.length}
                    rowsPerPage={this.state.rowsPerPage}
                    page={this.state.page}
                    onPageChange={this.handleChangePage}
                    onRowsPerPageChange={this.handleChangeRowsPerPage}
                />
            </div>
        );
    }


}

export default ProductTable;
