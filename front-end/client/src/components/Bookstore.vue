<template>
  <div class="hello">
    <b-container fluid>
      <b-row>
        <b-col sm="10">
          <b-form-input
              v-model="searchTerm"
              @keyup.enter="search"
              placeholder="input author name, then press Enter to search"></b-form-input>
          <p class="mt-1">Found {{ books.length }} results.</p>
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="7">
          <b-row>
            <h3>Search Results</h3>
            <b-pagination
                size="sm"
                v-model="searchCurrentPage"
                :total-rows="searchRows"
                :per-page="searchPerPage"
                aria-controls="search-table"></b-pagination>

            <p class="mt-1">Current Page: {{ searchCurrentPage }}</p>

            <b-table
                striped hover small
                id="search-table"
                :items="books"
                :fields="bookFields"
                :per-page="searchPerPage"
                :current-page="searchCurrentPage"></b-table>
          </b-row>
        </b-col>
        <b-col sm="3">
          <b-row>
            <h3>Search History</h3>
            <b-pagination
                size="sm"
                v-model="historyCurrentPage"
                :total-rows="historyRows"
                :per-page="historyPerPage"
                aria-controls="history-table"></b-pagination>

            <p class="mt-1">Current Page: {{ historyCurrentPage }}</p>

            <b-table
                striped hover small
                id="history-table"
                :items="histories"
                :fields="historyFields"
                :per-page="historyPerPage"
                :current-page="historyCurrentPage"></b-table>
          </b-row>
        </b-col>
      </b-row>
    </b-container>

    <ul v-if="errors && errors.length">
      <li v-for="error of errors" :key="error">
        {{ error.message }}
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'
import FingerprintJS from "@fingerprintjs/fingerprintjs"

export default {
  name: 'Bookstore',
  props: {
    // msg: String
  },
  data() {
    return {
      visitorId: '',
      searchTerm: '',
      searchCurrentPage: 1,
      searchPerPage: 20,
      books: [],
      bookFields: [
        {key: 'title'},
        {key: 'authors', formatter: 'formatAuthors'}
      ],
      historyCurrentPage: 1,
      historyPerPage: 10,
      histories: [],
      historyFields: [
        {key: 'title'},
        {key: 'authors', formatter: 'formatAuthors'}
      ],
      errors: []
    }
  },
  computed: {
    searchRows() {
      return this.books.length
    },
    historyRows() {
      return this.histories.length
    }
  },
  methods: {
    formatAuthors(authors) {
      return authors.join(', ')
    },
    search: function () {
      axios.post(
          'http://localhost:8080/services/books/v1/search',
          {authorName: this.searchTerm},
          {
            headers: {
              'content-type': 'application/json',
              'x-client-id': this.visitorId
            }
          }
      ).then(response => {
        this.books = response.data.payload
        this.searchCurrentPage = 1

        axios.get(
            'http://localhost:8080/services/books/v1/search/histories',
            {
              headers: {
                'content-type': 'application/json',
                'x-client-id': this.visitorId
              }
            }
        ).then(response => {
          this.histories = response.data.payload
        }).catch(e => {
          this.errors.push(e)
        })
      }).catch(e => {
        this.errors.push(e)
      })
    }
  },
  created() {
    FingerprintJS.load()
        .then((fp) => fp.get())
        .then((result) => {
          this.visitorId = result.visitorId;

          axios.get(
              'http://localhost:8080/services/books/v1/search/histories',
              {
                headers: {
                  'content-type': 'application/json',
                  'x-client-id': this.visitorId
                }
              }
          ).then(response => {
            this.histories = response.data.payload
          }).catch(e => {
            this.errors.push(e)
          })
        })
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
