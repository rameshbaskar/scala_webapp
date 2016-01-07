package controllers

import models.Contact
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import javax.inject.Inject

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = list()

  val contactForm = Form(
    tuple(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "emailAddress" -> nonEmptyText
    )
  )

  def list() = Action {
    Ok(views.html.index(Contact.list(), contactForm))
  }

  def create() = Action { implicit request =>
    contactForm.bindFromRequest().fold(
      errors => BadRequest(views.html.index(Contact.list(), errors)),
      {
        case (firstName, lastName, emailAddress) =>
          Contact.create(firstName, lastName, emailAddress)
          Redirect(routes.Application.list())
      }
    )
  }

  def delete(id: Int) = Action {
    Contact.delete(id)
    Redirect(routes.Application.list())
  }
}
