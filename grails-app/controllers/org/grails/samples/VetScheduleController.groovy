package org.grails.samples

import org.grails.plugin.easygrid.Easygrid
import org.grails.plugin.easygrid.Filter
import org.grails.plugin.easygrid.FilterOperatorsEnum
import org.grails.plugin.easygrid.JsUtils

import static org.grails.plugin.easygrid.GormUtils.applyFilter
import static org.grails.plugin.easygrid.GormUtils.applyFilter
import static org.grails.plugin.easygrid.GormUtils.applyFilter
import static org.grails.plugin.easygrid.GormUtils.applyFilter
import static org.grails.plugin.easygrid.GormUtils.applyFilter
import static org.grails.plugin.easygrid.GormUtils.applyFilter

@Easygrid
class VetScheduleController {

    def vetsGrid = {
        domainClass Vet
        columns {
            id {
                type 'id'
            }
            firstName
            lastName
            specialities {
                label 'vet.specialities.label'
                value { Vet vet ->
                    vet.specialities.collect { it.name }.join(',')
                }
                filterClosure { Filter filter ->
                    specialities {
                        applyFilter(delegate, filter.operator, 'name', filter.value)
                    }
                }
            }
            nrOfVisits {
                label 'vet.nrOfVisits.label'
                name 'nrOfVisits'
                value { Vet vet ->
                    Visit.countByVet(vet)
                }
                enableFilter false
            }
        }
        autocomplete {
            labelValue { val, params ->
                "${val.firstName} ${val.lastName}"
            }
            textBoxFilterClosure {
                or {
                    applyFilter(delegate, FilterOperatorsEnum.CN, 'firstName', params.term)
                    applyFilter(delegate, FilterOperatorsEnum.CN, 'lastName', params.term)
                }
            }
        }
    }

    def scheduleGrid = {
        domainClass Vet
        subGrid 'visitsSchedule'
        initialCriteria {
        }
        filterForm {
            fields {
                firstName
            }
        }
        columns {
            id {
                type 'id'
            }
            name {
                value { "${it.firstName} ${it.lastName}" }
                filterClosure { Filter filter ->
                    or {
                        applyFilter(delegate, filter.operator, 'firstName', filter.value)
                        applyFilter(delegate, filter.operator, 'lastName', filter.value)
                    }
                }
                sortClosure { sortOrder ->
                    order('lastName', sortOrder)
                    order('firstName', sortOrder)
                }
            }
            speciality {
                sortable false
                value { Vet vet -> vet.specialities.collect { it.name }.join(",") }
                filterDataType Long
                filterClosure { Filter filter ->
                    specialities {
                        applyFilter(delegate, filter.operator, 'id', filter.value)
                    }
                }
            }
        }
    }

    def visitsScheduleGrid = {
        domainClass Visit
        initialCriteria {
            //optimize the query
            join 'pet'
            join 'pet.owner'
            join 'pet.type'
        }
        globalFilterClosure { params ->
            vet {
                eq('id', params.id ? params.id as long : -1l)
            }
        }
        jqgrid {
            sortname 'date'
            sortorder 'desc'
        }
        columns {
            'pet.name' {
                label 'Pet'
            }
            'pet.type.name' {
                label 'Type'
                filterDataType Long
                filterProperty 'pet.type.id'
                jqgrid {
                    stype = 'select'
                    searchoptions {
                        sopt(['eq'])
                        value(JsUtils.convertListToString { PetType.listOrderByName() })
                    }
                }
            }
            'pet.owner.name' {
                label 'Owner'
                value {
                    "${it.pet.owner.firstName} ${it.pet.owner.lastName}"
                }
                filterClosure { Filter filter ->
                    pet {
                        owner {
                            or {
                                applyFilter(delegate, filter.operator, 'firstName', filter.value)
                                applyFilter(delegate, filter.operator, 'lastName', filter.value)
                            }
                        }
                    }
                }
                sortClosure { sortOrder ->
                    pet {
                        owner {
                            order('lastName', sortOrder)
                            order('firstName', sortOrder)
                        }
                    }
                }
            }
            date {
                formatter {
                    g.formatDate(date: it, format: "yyyy-MM-dd")
                }
            }
            description
        }
    }

    def visitsOverviewGrid = {
        domainClass Visit
        initialCriteria {
            projections {
                max('date')
                vet {
                    groupProperty('id')
                    property('firstName')
                    property('lastName')
                }
                pet {
                    groupProperty('type', 'pt')
                }
                count()
                min('date')
            }
        }
        transformData { row ->
            def result = [:]
            result.minDate = row[6]
            result.maxDate = row[0]
            result.firstName = row[2]
            result.lastName = row[3]
            result['pet.type'] = row[4]
            result['nrVisits'] = row[5]
            result
        }
        columns {
            name {
                value {
                    "${it.firstName}, ${it.lastName}"
                }
                filterDataType String
                filterClosure { filter->
                    vet {
                        or {
                            applyFilter(delegate, filter.operator, 'firstName', filter.value)
                            applyFilter(delegate, filter.operator, 'lastName', filter.value)
                        }
                    }
                }
                sortClosure { sortOrder ->
                    vet {
                        order('lastName', sortOrder)
                        order('firstName', sortOrder)
                    }
                }
            }
            'pet.type' {
                filterDataType Long
                filterProperty 'pet.type.id'
                value {
                    it['pet.type'].name
                }
            }
            nrVisits {
                sortable false
                enableFilter false
            }
            minDate {
                sortable false
                filterDataType Date
                filterProperty 'date'
                sortProperty 'date'
                defaultFilterOperator FilterOperatorsEnum.GE
                formatter {
                    g.formatDate(date: it, format: "yyyy-MM-dd")
                }
            }
            maxDate {
                sortable false
                filterDataType Date
                filterProperty 'date'
                sortProperty 'date'
                defaultFilterOperator FilterOperatorsEnum.LE
                formatter {
                    g.formatDate(date: it, format: "yyyy-MM-dd")
                }
            }
        }
    }

    def index() {}

    def overview() {}
}
