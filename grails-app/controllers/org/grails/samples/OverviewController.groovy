package org.grails.samples

import org.grails.plugin.easygrid.Easygrid
import org.grails.plugin.easygrid.Filter

import static org.grails.plugin.easygrid.GormUtils.applyFilter

@Easygrid
class OverviewController {

    def ownersGrid = {
        domainClass Owner
        columns {
            id {
                type 'id'
                enableFilter false
            }
            firstName
            lastName
            address
            city
            telephone
            nrPets {
                enableFilter false
                value { owner ->
                    owner.pets.size()
                }
                jqgrid {
                    sortable false
                }
            }
        }
    }

    def petsGrid = {
        domainClass Pet
        globalFilterClosure { params ->
            //link to the owner provided by the master table
            eq('owner.id', params.ownerId ? params.ownerId as long : -1l)
        }
        columns {
            id {
                type 'id'
                enableFilter false
            }
            name
            birthDate
            'type.name' {
            }
            nrVisits {
                enableFilter false
                value { Pet pet ->
                    pet.visits.size()
                }
            }
        }
    }

    def visitsGrid = {
        domainClass Visit
        inlineEdit false
        globalFilterClosure { params ->
            eq('pet.id', params.petId ? params.petId as long : -1l)
        }
        columns {
            vet {
                value { Visit visit ->
                    "${visit.vet.firstName} ${visit.vet.lastName}"
                }
                jqgrid {
                    editable false
                }
                filterClosure { Filter filter ->
                    vet {
                        or {
                            applyFilter(delegate, filter.operator, 'firstName', filter.value)
                            applyFilter(delegate, filter.operator, 'lastName', filter.value)
                        }
                    }
                }
                sortClosure { sortOrder ->
                    order('vet.lastName', sortOrder)
                    order('vet.firstName', sortOrder)
                }
            }
            description
            date
        }
    }


    def index() {
    }


}
