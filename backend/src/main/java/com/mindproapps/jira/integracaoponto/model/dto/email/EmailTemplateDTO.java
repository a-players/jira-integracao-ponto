package com.mindproapps.jira.integracaoponto.model.dto.email;

public class EmailTemplateDTO {
    public static final String EMAIL_TEMPLATE =
            "<!DOCTYPE html\n" +
                    "    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "\n" +
                    "<head>\n" +
                    "    <title>Email Template</title>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale==1\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IEedge\" />\n" +
                    "\n" +
                    "    <style type=\"text/css\">\n" +
                    "        /* CLIENT-SPECIFIC STYLES */\n" +
                    "        \n" +
                    "        body,\n" +
                    "        table,\n" +
                    "        td,\n" +
                    "        a {\n" +
                    "            -webkit-text-size-adjust: 100%;\n" +
                    "            -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none;\n" +
                    "        }\n" +
                    "        /* Prevent WebKit and Windows mobile changing default text sizes */\n" +
                    "        \n" +
                    "        table,\n" +
                    "        td {\n" +
                    "            mso-table-lspace: 0pt;\n" +
                    "            mso-table-rspace: 0pt;\n" +
                    "        }\n" +
                    "        /* Remove spacing between tables in Outlook 2007 and up */\n" +
                    "        \n" +
                    "        img {\n" +
                    "            -ms-interpolation-mode: bicubic;\n" +
                    "        }\n" +
                    "        /* Allow smoother rendering of resized image in Internet Explorer */\n" +
                    "        /* RESET STYLES */\n" +
                    "        \n" +
                    "        img {\n" +
                    "            border: 0;\n" +
                    "            height: auto;\n" +
                    "            line-height: 100%;\n" +
                    "            outline: none;\n" +
                    "            text-decoration: none;\n" +
                    "        }\n" +
                    "        \n" +
                    "        table {\n" +
                    "            border-collapse: collapse !important;\n" +
                    "            display: inline-table;\n" +
                    "        }\n" +
                    "        \n" +
                    "        body {\n" +
                    "            height: 100% !important;\n" +
                    "            padding: 0 !important;\n" +
                    "            width: 100% !important;\n" +
                    "            background-color: #f8f8f8 !important;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .email-layout-table {\n" +
                    "            text-align: center;\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f8f8f8 !important;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .email-title {\n" +
                    "            height: 24px;\n" +
                    "            color: #12485A;\n" +
                    "            font-size: 20px;\n" +
                    "            font-weight: bold;\n" +
                    "            line-height: 24px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .email-subject-info {\n" +
                    "            height: 24px;\n" +
                    "            color: #333333;\n" +
                    "            font-size: 18px;\n" +
                    "            line-height: 24px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .email-comment-title {\n" +
                    "            text-align: left;\n" +
                    "            height: 25px;\n" +
                    "            width: 86px;\n" +
                    "            color: #FF7300;\n" +
                    "            font-size: 14px;\n" +
                    "            font-weight: bold;\n" +
                    "            line-height: 25px;\n" +
                    "            text-transform: uppercase;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .email-comment-info {\n" +
                    "            height: 16px;\n" +
                    "            color: #333333;\n" +
                    "            font-size: 14px;\n" +
                    "            line-height: 16px;\n" +
                    "            text-align: left;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .standard-button {\n" +
                    "            border: 1px solid #D9D9D9;\n" +
                    "            background-color: #F8F8F8;\n" +
                    "            box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.05);\n" +
                    "            color: #555555;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 3px;\n" +
                    "            display: inline-block;\n" +
                    "            padding: 8px 20px;\n" +
                    "            width: 120px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .primary-button {\n" +
                    "            border-radius: 3px;\n" +
                    "            background-color: #0073EC;\n" +
                    "            box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.05);\n" +
                    "            color: #ffffff;\n" +
                    "            text-decoration: none;\n" +
                    "            padding: 10px 25px;\n" +
                    "            border: 1px solid #0072ed;\n" +
                    "            display: inline-block;\n" +
                    "            width: 120px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .line-space {\n" +
                    "            padding-top: 16px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .block-content-table {\n" +
                    "            border: 1px solid #ececec;\n" +
                    "            max-width: 600px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .block-content-container {\n" +
                    "            padding: 24px 40px;\n" +
                    "            border: 1px solid transparent;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .block-content-table .line-info-left {\n" +
                    "            height: 16px;\n" +
                    "            color: #333333;\n" +
                    "            font-size: 14px;\n" +
                    "            line-height: 16px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .block-content-table .line-info-right {\n" +
                    "            height: 16px;\n" +
                    "            color: #333333;\n" +
                    "            font-size: 14px;\n" +
                    "            font-weight: bold;\n" +
                    "            line-height: 16px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .block-content-table .line-info-right .line-info-right-subinfo {\n" +
                    "            font-weight: normal;\n" +
                    "            padding-left: 8px;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .footer-subject-info {\n" +
                    "            height: 12px;\n" +
                    "            width: 536px;\n" +
                    "            color: #999999;\n" +
                    "            font-size: 11px;\n" +
                    "            line-height: 12px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        \n" +
                    "        .top-padding {\n" +
                    "            padding: 32px 0 0 0;\n" +
                    "        }\n" +
                    "        /* iOS BLUE LINKS */\n" +
                    "        \n" +
                    "        a[x-apple-data-detectors] {\n" +
                    "            color: inherit !important;\n" +
                    "            text-decoration: none !important;\n" +
                    "            font-size: inherit !important;\n" +
                    "            font-family: inherit !important;\n" +
                    "            font-weight: inherit !important;\n" +
                    "            line-height: inherit !important;\n" +
                    "        }\n" +
                    "        /* MOBILE STYLES */\n" +
                    "        \n" +
                    "        @media screen and (max-width: 525px) {\n" +
                    "            /* ALLOWS FOR FLUID TABLES */\n" +
                    "            .wrapper {\n" +
                    "                width: 95% !important;\n" +
                    "                max-width: 95% !important;\n" +
                    "            }\n" +
                    "            .top-padding {\n" +
                    "                padding: 16px 0 0 0 !important;\n" +
                    "            }\n" +
                    "            /* ADJUSTS LAYOUT OF LOGO IMAGE */\n" +
                    "            .logo img {\n" +
                    "                margin: 0 auto !important;\n" +
                    "            }\n" +
                    "            .block-content-mobile {\n" +
                    "                padding: 10px 20px !important;\n" +
                    "            }\n" +
                    "            /* USE THESE CLASSES TO HIDE CONTENT ON MOBILE */\n" +
                    "            .mobile-hide {\n" +
                    "                display: none !important;\n" +
                    "            }\n" +
                    "            .img-max {\n" +
                    "                max-width: 100% !important;\n" +
                    "                width: 100% !important;\n" +
                    "                height: auto !important;\n" +
                    "            }\n" +
                    "            /* FULL-WIDTH TABLES */\n" +
                    "            .responsive-table {\n" +
                    "                width: 100% !important;\n" +
                    "            }\n" +
                    "            /* UTILITY CLASSES FOR ADJUSTING PADDING ON MOBILE */\n" +
                    "            .padding {\n" +
                    "                padding: 10px 5% 15px 5% !important;\n" +
                    "            }\n" +
                    "            .padding-meta {\n" +
                    "                padding: 30px 5% 0px 5% !important;\n" +
                    "                text-align: center;\n" +
                    "            }\n" +
                    "            .table-header-mobile {\n" +
                    "                padding: 10px 0 15px 0;\n" +
                    "            }\n" +
                    "            .no-padding {\n" +
                    "                padding: 0 !important;\n" +
                    "            }\n" +
                    "            .section-padding {\n" +
                    "                padding: 50px 15px 50px 15px !important;\n" +
                    "            }\n" +
                    "            /* ADJUST BUTTONS ON MOBILE */\n" +
                    "            .mobile-button-container {\n" +
                    "                margin: 0 auto;\n" +
                    "                width: 100% !important;\n" +
                    "            }\n" +
                    "            .mobile-button {\n" +
                    "                padding: 10px !important;\n" +
                    "                font-size: 16px !important;\n" +
                    "                display: block !important;\n" +
                    "                width: 85% !important;\n" +
                    "            }\n" +
                    "            .primary-button-container {\n" +
                    "                display: inline-block;\n" +
                    "                width: 80%;\n" +
                    "                padding: 15px 10%;\n" +
                    "            }\n" +
                    "            .secondary-button-container {\n" +
                    "                display: inline-block;\n" +
                    "                width: 80%;\n" +
                    "                padding: 0 10%;\n" +
                    "            }\n" +
                    "        }\n" +
                    "        /* ANDROID CENTER FIX */\n" +
                    "        \n" +
                    "        div[style*=\"margin: 16px 0;\"] {\n" +
                    "            margin: 0 !important;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; height: 100%\n" +
                    "  !important; padding: 0 !important; width: 100% !important; background-color: #f8f8f8 !important; \">\n" +
                    "    <table class=\" email-layout-table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"\n" +
                    "-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "mso-table-rspace: 0pt; display: inline-table; text-align: center; font-family: Arial, sans-serif; border-collapse:\n" +
                    "collapse !important; background-color: #f8f8f8 !important; \">\n" +
                    "\n" +
                    "        <tr>\n" +
                    "            <td style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace:\n" +
                    "0pt; mso-table-rspace: 0pt; \">\n" +
                    "                <div class=\" top-padding\" style=\"padding-top: 32px;\">\n" +
                    "                </div>\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"block-content-table wrapper\" style=\"\n" +
                    "-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "mso-table-rspace: 0pt; display: inline-table; border: 1px solid #ececec; max-width: 600px; border-collapse: collapse\n" +
                    "!important;\">\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"block-content-container block-content-mobile\" bgcolor=\"#ffffff\" align=\"center\" style=\" padding:\n" +
                    "24px 40px; border: 1px solid transparent; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\n" +
                    "\"0\" width=\"600\">\n" +
                    "<tr>\n" +
                    "  <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "<![endif]-->\n" +
                    "                            <table class=\"responsive-table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"\n" +
                    "  max-width: 500px; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "  mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "      mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                        <!-- COPY -->\n" +
                    "                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td align=\"center\" class=\"padding-copy email-title\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "            -ms-text-size-adjust: 100%; text-decoration: none; height: 24px; color: #12485A; font-size: 20px;\n" +
                    "            font-weight: bold; line-height: 24px; text-align: center; padding: 10px 5% 10px 5% !important;\n" +
                    "            mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <span>%%TIMESHEET_STATUS%%</span>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                            <tr>\n" +
                    "                                                <td align=\"center\" class=\"padding-copy email-subject-in=fo\" style=\" height: 24px; color:\n" +
                    "            #333333; font-size: 18px; line-height: 24px; text-align: center; -webkit-text-size-adjust: 100%;\n" +
                    "            -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <span><b>%%APPROVER_NAME%%</b> %%APPROVER_ACTION%%</span>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                            <tr>\n" +
                    "                                                <td style=\"color:#FFFFFF; font-size: 0;\" title=\"1608224=766860\"></td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "<![endif]-->\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "                <div class=\"line-space\" style=\"padding-top: 16px;\"></div>\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"block-content-table wrapper\" style=\"\n" +
                    "-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "mso-table-rspace: 0pt; display: inline-table; border: 1px solid #ececec; max-width: 530px; border-collapse: collapse\n" +
                    "!important;\">\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"center\" class=\"block-content-mobile\" style=\" border: 1px solid transparent;\n" +
                    "padding: 18px 40px; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding==\n" +
                    "\"0\" width=\"570\">\n" +
                    "<tr>\n" +
                    "  <td align=\"center\" valign=\"top\" width=\"570\">\n" +
                    "<![endif]-->\n" +
                    "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"responsive-table\" style=\"\n" +
                    "  max-width: 570px; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "  mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "      mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                        <!-- COPY -->\n" +
                    "                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td class=\"padding-copy email-comment-title\" style=\" text-align: left; height: 25px; width: 86px;\n" +
                    "            color: #FF7300; font-size: 14px; font-weight: bold; line-height: 25px; text-transform: uppercase;\n" +
                    "            -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace:\n" +
                    "            0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <span>%%COMMENT_TITLE%%</span>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                            <tr>\n" +
                    "                                                <td class=\"padding-copy email-comment-info\" style=\" height: 16px; color: #333333; font-size: 14px;\n" +
                    "            line-height: 16px; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration:\n" +
                    "            none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <span>%%COMMENT_CONTENT%%</span>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "<![endif]-->\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "                <div class=\"line-space\" style=\"padding-top: 16px;\"></div>\n" +
                    "\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"block-content-table wrapper\" style=\"\n" +
                    "-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "mso-table-rspace: 0pt; display: inline-table; border: 1px solid #ececec; max-width: 600px; border-collapse: collapse\n" +
                    "!important;\">\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"block-content-container block-content-mobile\" bgcolor=\"#ffffff\" align=\"center\" style=\" padding:\n" +
                    "24px 40px; border: 1px solid transparent; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "<table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding==\n" +
                    "\"0\" width=\"600\">\n" +
                    "<tr>\n" +
                    "  <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "<![endif]-->\n" +
                    "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"responsive-table\" style=\"\n" +
                    "  max-width: 600px; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "  mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" padding: 15px 0 0 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "      text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                        <!-- TWO COLUMNS -->\n" +
                    "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td valign=\"top\" style=\" padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <!-- LEFT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"left\" style=\"\n" +
                    "              width: 37%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td align=\"right\" style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%;\n" +
                    "                  -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace:\n" +
                    "                  0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"left\" class=\"line-info-left\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; line-height:\n" +
                    "                        16px;\">\n" +
                    "                                                                            <span>%%PERIOD_TITLE%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                    <!-- RIGHT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"right\" style=\"\n" +
                    "              width: 57%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "                  text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"right\" class=\"line-info-right\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; font-weight: bold;\n" +
                    "                        line-height: 16px;\">\n" +
                    "                                                                            <span>%%PERIOD_CONTENT%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" padding: 15px 0 0 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "      text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;border-top: 1px solid #eaeaea;\">\n" +
                    "                                        <!-- TWO COLUMNS -->\n" +
                    "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td valign=\"top\" style=\" padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <!-- LEFT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"left\" style=\"\n" +
                    "              width: 37%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td align=\"right\" style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%;\n" +
                    "                  -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace:\n" +
                    "                  0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"left\" class=\"line-info-left\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; line-height:\n" +
                    "                        16px;\">\n" +
                    "                                                                            <span>%%USER_TITLE%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                    <!-- RIGHT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"right\" style=\"\n" +
                    "              width: 57%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "                  text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"right\" class=\"line-info-right\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; font-weight: bold;\n" +
                    "                        line-height: 16px;\">\n" +
                    "                                                                            <span>%%USER_CONTENT%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" padding: 15px 0 0 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "      text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;border-top: 1px solid #eaeaea;\">\n" +
                    "                                        <!-- TWO COLUMNS -->\n" +
                    "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td valign=\"top\" style=\" padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <!-- LEFT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"left\" style=\"\n" +
                    "              width: 37%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td align=\"right\" style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%;\n" +
                    "                  -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace:\n" +
                    "                  0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"left\" class=\"line-info-left\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; line-height:\n" +
                    "                        16px;\">\n" +
                    "                                                                            <span>%%HOURS_REQUIRED_TITLE%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                    <!-- RIGHT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"right\" style=\"\n" +
                    "              width: 57%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "                  text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"right\" class=\"line-info-right\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; font-weight: bold;\n" +
                    "                        line-height: 16px;\">\n" +
                    "                                                                            <span>%%HOURS_REQUIRED_CONTENT%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" padding: 15px 0 0 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "      text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;border-top: 1px solid #eaeaea;\">\n" +
                    "                                        <!-- TWO COLUMNS -->\n" +
                    "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td valign=\"top\" style=\" padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <!-- LEFT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"left\" style=\"\n" +
                    "              width: 37%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td align=\"right\" style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%;\n" +
                    "                  -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace:\n" +
                    "                  0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"left\" class=\"line-info-left\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; line-height:\n" +
                    "                        16px;\">\n" +
                    "                                                                            <span>%%HOURS_LOGGED_TITLE%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                    <!-- RIGHT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"right\" style=\"\n" +
                    "              width: 57%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "                  text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"right\" class=\"line-info-right\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; font-weight: bold;\n" +
                    "                        line-height: 16px;\">\n" +
                    "                                                                            <span>%%HOURS_LOGGED_CONTENT%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" padding: 15px 0 0 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "      text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;border-top: 1px solid #eaeaea;\">\n" +
                    "                                        <!-- TWO COLUMNS -->\n" +
                    "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\" -webkit-text-size-adjust:\n" +
                    "        100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\n" +
                    "        display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td valign=\"top\" style=\" padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <!-- LEFT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"left\" style=\"\n" +
                    "              width: 37%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td align=\"right\" style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%;\n" +
                    "                  -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace:\n" +
                    "                  0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"left\" class=\"line-info-left\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; line-height:\n" +
                    "                        16px;\">\n" +
                    "                                                                            <span>%%REVIEWER_TITLE%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                    <!-- RIGHT COLUMN -->\n" +
                    "                                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"=45%\" align=\"right\" style=\"\n" +
                    "              width: 57%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td style=\" padding: 0 0 10px 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "                  text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" wid=th=\"100%\" style=\"\n" +
                    "                    -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"right\" class=\"line-info-right\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                        -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "                        mso-table-rspace: 0pt; height: 16px; color: #333333; font-size: 14px; font-weight: bold;\n" +
                    "                        line-height: 16px;\">\n" +
                    "                                                                            <span>%%REVIEWER_CONTENT%%</span>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "<![endif]-->\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" align=\"center\" style=\" padding-bottom: 24px; border: 1px solid transparent;\n" +
                    "-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "mso-table-rspace: 0pt;\">\n" +
                    "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"500\" c=lass=\"responsive-table\" style=\"\n" +
                    "  -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "  mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "      mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding==\"0\" style=\"\n" +
                    "        -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace:\n" +
                    "        0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td align=\"center\" style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "            text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                    <!-- BULLETPROOF BUTTON -->\n" +
                    "                                                    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpa=dding=\"0\" style=\"\n" +
                    "              -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "              mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse\n" +
                    "              !important;\">\n" +
                    "                                                        <tr>\n" +
                    "                                                            <td align=\"center\" class=\"padding\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "                  -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace:\n" +
                    "                  0pt;\">\n" +
                    "                                                                <table border=\"0\" cellspacing=\"0\" cellpadding=\"=0\" class=\"mobile-button-container\" style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none;\n" +
                    "                    mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table; border-collapse:\n" +
                    "                    collapse !important;\">\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td align=\"center\" style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "                        text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                                                                            <a class=\"mobile-button standard-button\" href=\"%%TIMESHEET_URL%%\" style=\" -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration:\n" +
                    "                          none; border: 1px solid #D9D9D9; background-color: #F8F8F8; box-shadow: 0 1px 1px 0\n" +
                    "                          rgba(0,0,0,0.05); color: #555555; border-radius: 3px; display: inline-block; padding:\n" +
                    "                          8px 20px; width: 120px;\">%%URL_LABEL%%</a>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </table>\n" +
                    "                                                            </td>\n" +
                    "                                                        </tr>\n" +
                    "                                                    </table>\n" +
                    "                                                </td>\n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "<![endif]-->\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "\n" +
                    "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"wrapper\" style=\"\n" +
                    "-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt;\n" +
                    "mso-table-rspace: 0pt; display: inline-table; border-collapse: collapse !important;\">\n" +
                    "                    <tr>\n" +
                    "                        <td align=\"center\" style=\" padding: 20px 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;\n" +
                    "text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\">\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "<table\n" +
                    "  align=\"center\"\n" +
                    "  border=\"0\"\n" +
                    "  cellspacing=\"0\"\n" +
                    "  cellpadding=\"0\"\n" +
                    "  width=\"500\"\n" +
                    ">\n" +
                    "<tr>\n" +
                    "  <td align=\"center\" valign=\"top\" width=\"500\">\n" +
                    "<![endif]-->\n" +
                    "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" class=\"responsive-table\" style=\" max-width: 500px; -webkit-text-size-adjust: 100%; -ms-text-size-adjust:\n" +
                    "  100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-table;\n" +
                    "  border-collapse: collapse !important;\">\n" +
                    "                                <tr>\n" +
                    "                                    <td align=\"center\" class=\"footer-subject-info\" style=\" -webkit-text-size-adjust: 100%;\n" +
                    "      -ms-text-size-adjust: 100%; text-decoration: none; mso-table-lspace: 0pt; mso-table-rspace: 0pt; height:\n" +
                    "      12px; width: 536px; color: #999999; font-size: 11px; line-height: 12px; text-align: center;\">\n" +
                    "                                        <span>%%SENT_BY%%</span>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                            <!--[if (gte mso 9)|(IE)]>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "<![endif]-->\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "\n" +
                    "\n" +
                    "    </table>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";
}
