package twodomainstest

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class Domain1Controller {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Domain1.list(params), model:[domain1Count: Domain1.count()]
    }

    def show(Domain1 domain1) {
        respond domain1
    }

    def create() {
        respond new Domain1(params)
    }

    @Transactional
    def save(Domain1 domain1) {
        if (domain1 == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (domain1.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond domain1.errors, view:'create'
            return
        }

        domain1.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'domain1.label', default: 'Domain1'), domain1.id])
                redirect domain1
            }
            '*' { respond domain1, [status: CREATED] }
        }
    }

    def edit(Domain1 domain1) {
        respond domain1
    }

    @Transactional
    def update(Domain1 domain1) {
        if (domain1 == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (domain1.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond domain1.errors, view:'edit'
            return
        }

        domain1.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'domain1.label', default: 'Domain1'), domain1.id])
                redirect domain1
            }
            '*'{ respond domain1, [status: OK] }
        }
    }

    @Transactional
    def delete(Domain1 domain1) {

        if (domain1 == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        domain1.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'domain1.label', default: 'Domain1'), domain1.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'domain1.label', default: 'Domain1'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
