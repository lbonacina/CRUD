CRUD
========================

some notes on CRUD

-   using request scope for edit is complicated since information about the selected bean that are not on the edit
    field (for example the id of the record) are not mantained.
    this could be avoided if necessary using a hidden field

-   watch out for a single-page crud (table+fields on same page) since when you try for example 'edit'
    the whole form is submitted, including the field which might result in some constraint violation
    usa partial submit with ajax