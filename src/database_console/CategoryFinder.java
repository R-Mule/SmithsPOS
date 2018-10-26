package database_console;

import java.io.Serializable;

/**
 *
 * @author A.Smith
 */
public class CategoryFinder implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    CategoryFinder() {

    }//end ctor

    public String getCategory(int catCode) {
        String category;
        switch (catCode) {
            case 11:
                category = "ANALGESIC,INTERNAL";
                break;
            case 12:
                category = "ANALGESIC,EXTERNAL";
                break;
            case 31:
                category = "ANTACIDS,LIQUIDS";
                break;
            case 32:
                category = "ANTACIDS,OTHER";
                break;
            case 41:
                category = "AUTOMOTIVE SUPPLIES";
                break;
            case 51:
                category = "BABY FOOD";
                break;
            case 71:
                category = "BABY CARE";
                break;
            case 73:
                category = "BABY FEEDING ACCESSORIES";
                break;
            case 74:
                category = "BABY DISPOSABLE DIAPERS";
                break;
            case 92:
                category = "SOFT DRINKS";
                break;
            case 93:
                category = "CARRY OUT FOOD AND ICE CREAM";
                break;
            case 111:
                category = "COSMETICS FOR LIPS";
                break;
            case 112:
                category = "COSMETICS FOR FACE";
                break;
            case 113:
                category = "COSMETICS FOR EYES";
                break;
            case 131:
                category = "COSMETICS,FRANCHISE";
                break;
            case 151:
                category = "COUGH AND COLD LIQUIDS";
                break;
            case 152:
                category = "COLD RUBS,INHALENTS,LOZENGES";
                break;
            case 153:
                category = "COLD TABLETS AND CAPSULES";
                break;
            case 154:
                category = "NOSE DROPS AND SPRAY";
                break;
            case 171:
                category = "DEODORANTS, AEROSOL";
                break;
            case 173:
                category = "DEODORANTS, OTHER";
                break;
            case 191:
                category = "SUGAR AND SALT SUBSTITUTES";
                break;
            case 192:
                category = "FOODS,SPECIAL OR SUPPLEMENTARY";
                break;
            case 193:
                category = "WEIGHT CONTROL";
                break;
            case 201:
                category = "RADIOS AND TV";
                break;
            case 202:
                category = "ELECTRICAL APPLIANCES";
                break;
            case 203:
                category = "RECORDS AND SOUND TAPES";
                break;
            case 204:
                category = "ELECTRIC LAMPS AND ACCESSORIES";
                break;
            case 205:
                category = "BATTERIES";
                break;
            case 211:
                category = "CONTACT LENS PREPARATIONS";
                break;
            case 212:
                category = "EYE PREPARATIONS";
                break;
            case 213:
                category = "EAR PREPARATIONS";
                break;
            case 231:
                category = "SANITARY NAPKINS AND TAMPONS";
                break;
            case 233:
                category = "FEMININE DEODORANTS";
                break;
            case 234:
                category = "FEMININE SYRINGES AND DOUCHES";
                break;
            case 236:
                category = "VAGINAL JELLY AND CREAMS";
                break;
            case 257:
                category = "PROPHYLACTICS";
                break;
            case 251:
                category = "FIRST AID DRESSING";
                break;
            case 252:
                category = "FIRST AID TREATMENTS";
                break;
            case 254:
                category = "ELASTIC GOODS";
                break;
            case 271:
                category = "FOOT PADS";
                break;
            case 272:
                category = "FOOT PRODUCTS";
                break;
            case 291:
                category = "GIFTS";
                break;
            case 292:
                category = "JEWELRY";
                break;
            case 293:
                category = "WATCHS AND CLOCKS";
                break;
            case 311:
                category = "COMBS,BRUSHES AND ROLLERS";
                break;
            case 312:
                category = "WIGS AND FALLS";
                break;
            case 313:
                category = "HAIR CURLERS AND DRYERS";
                break;
            case 331:
                category = "SHAMPOO";
                break;
            case 332:
                category = "PERMANENTS AND STRAIGHTENERS";
                break;
            case 333:
                category = "HAIR SPRAY";
                break;
            case 334:
                category = "HAIR SETTING";
                break;
            case 335:
                category = "HAIR COLOR";
                break;
            case 336:
                category = "ETHNIC HAIR PRODUCTS";
                break;
            case 351:
                category = "INSECTICIDES & ROOM FRESHENERS";
                break;
            case 354:
                category = "HARDWARE";
                break;
            case 357:
                category = "CLEANERS SOAPS AND SHOE WAXES";
                break;
            case 358:
                category = "HOUSEWARES AND FLASHLIGHTS";
                break;
            case 371:
                category = "LAXATIVE TABLETS AND LIQUIDS";
                break;
            case 372:
                category = "LAXATIVE,OTHER";
                break;
            case 391:
                category = "MANICURE AND PEDICURE AIDS";
                break;
            case 392:
                category = "NAIL POLISH AND POLISH REMOVER";
                break;
            case 411:
                category = "MENS COLOGNE AND AFTER SHAVE";
                break;
            case 412:
                category = "MENS HAIR PREPARATIONS";
                break;
            case 451:
                category = "TOOTH PASTE AND TOOTH POWDERS";
                break;
            case 452:
                category = "TOOTH BRUSHES AND DENTAL FLOSS";
                break;
            case 453:
                category = "DENTURE PRODUCTS";
                break;
            case 454:
                category = "MOUTH WASH AND GARGLE";
                break;
            case 455:
                category = "ORAL HYGIENE ACCESSORIES";
                break;
            case 471:
                category = "SEDATIVES AND STIMULANTS";
                break;
            case 472:
                category = "ASTHMA PREPARATIONS";
                break;
            case 473:
                category = "TABLETS AND CAPSULES";
                break;
            case 474:
                category = "WETS AND DRY";
                break;
            case 476:
                category = "OINTMENTS,CREAMS AND LIQUIDS";
                break;
            case 491:
                category = "FACIAL TISSUE";
                break;
            case 492:
                category = "TOILET TISSUE AND PAPER TOWELS";
                break;
            case 511:
                category = "PET AND ANIMAL FOOD";
                break;
            case 513:
                category = "PET AND ANIMAL CARE";
                break;
            case 531:
                category = "FILM";
                break;
            case 532:
                category = "PHOTO PROCESSING";
                break;
            case 533:
                category = "CAMERAS AND PROJECTORS";
                break;
            case 571:
                category = "SUN TAN PREPARATIONS";
                break;
            case 572:
                category = "INSECT REPELLENTS";
                break;
            case 573:
                category = "SUN GLASSES";
                break;
            case 574:
                category = "PICNIC,PATIO & GARDEN SUPPLIES";
                break;
            case 576:
                category = "HOLIDAY PROMOTIONS";
                break;
            case 577:
                category = "SPORTING GOODS";
                break;
            case 611:
                category = "BLADES AND RAZORS";
                break;
            case 612:
                category = "ELECTRIC RAZORS";
                break;
            case 613:
                category = "SHAVING CREAM";
                break;
            case 621:
                category = "BATHROOM SAFETY PRODUCTS";
                break;
            case 622:
                category = "DIABETES PRODUCTS";
                break;
            case 623:
                category = "DIAGNOSTIC PRODUCTS & SYRINGES";
                break;
            case 624:
                category = "DECUBITUS AND SKIN CARE";
                break;
            case 626:
                category = "EXERCISE EQUIPMENT AND SCALES";
                break;
            case 627:
                category = "HEALTH SUPPORTS";
                break;
            case 628:
                category = "HOME NUTRITIONAL THERAPY";
                break;
            case 629:
                category = "HOME TEST KITS";
                break;
            case 631:
                category = "HOSPITAL BEDS & ROOM SUPPLIES";
                break;
            case 632:
                category = "INCONTINENCE PRODUCTS";
                break;
            case 633:
                category = "ORTHOPEDIC APPLIANCES";
                break;
            case 634:
                category = "OSTOMY & MASTECTOMY PRODUCTS";
                break;
            case 635:
                category = "PATIENT AIDS FOR DAILY LIVING";
                break;
            case 636:
                category = "REHAB AND PHYSICAL THERAPY";
                break;
            case 637:
                category = "RESPIRATORY THERAPY AND OXYGEN";
                break;
            case 638:
                category = "RUBBER GOODS";
                break;
            case 639:
                category = "TENS AND ELECTROTHERAPY";
                break;
            case 641:
                category = "UROLOGIC PRODUCTS";
                break;
            case 642:
                category = "WALKING AIDS AND ACCESSORIES";
                break;
            case 643:
                category = "WHEELCHAIRS AND ACCESSORIES";
                break;
            case 651:
                category = "ACNE MEDICATIONS";
                break;
            case 652:
                category = "PERSONAL SOAP";
                break;
            case 653:
                category = "HAND-FACE LOTIONS AND CREAMS";
                break;
            case 654:
                category = "BATH PRODUCTS";
                break;
            case 655:
                category = "COTTON BALLS,SWABS AND PADS";
                break;
            case 656:
                category = "FRAGRANCE PRODUCTS";
                break;
            case 657:
                category = "DEPILATORY";
                break;
            case 658:
                category = "ETHNIC BLEACHING CREAM,LOTIONS";
                break;
            case 659:
                category = "LIP CARE & LIP BALMS";
                break;
            case 664:
                category = "IN/OUT PROMOTIONAL MERCHANDISE";
                break;
            case 671:
                category = "STATIONERY,BOXED OR PACKAGED";
                break;
            case 672:
                category = "WRITING INSTRUMENTS";
                break;
            case 673:
                category = "SCHOOL ACCESSORIES";
                break;
            case 674:
                category = "SCHOOL PAPER & NOTEBOOKS";
                break;
            case 691:
                category = "LEATHER GOODS";
                break;
            case 692:
                category = "SMOKING ACCESSORIES";
                break;
            case 693:
                category = "HOSIERY";
                break;
            case 701:
                category = "TOYS AND GAMES";
                break;
            case 731:
                category = "VITAMIN TABLETS";
                break;
            case 732:
                category = "VITAMINS, LIQUID";
                break;
            case 733:
                category = "VITAMINS, NATURAL";
                break;
            case 751:
                category = "CANDY";
                break;
            case 761:
                category = "MAGAZINES AND BOOKS";
                break;
            case 781:
                category = "GREETING CARDS";
                break;
            case 791:
                category = "SUPPLIES, STICKERS & REPORTS";
                break;
            case 792:
                category = "SUPPLIES, NO STICKERS/REPORTS";
                break;
            case 801:
                category = "PHARMACY, NON RX";
                break;
            case 851:
                category = "PHARMACY, RX";
                break;
            case 852:
                category = "OTC GENERAL";
                break;
            case 853:
                category = "RA PAYMENT";
                break;
            case 854:
                category = "DME RA PAYMENT";
                break;
            case 855:
                category = "NEWSPAPER";
                break;
            case 856:
                category = "AMERICAN GREETINGS";
                break;
            case 857:
                category = "POP";
                break;
            case 858:
                category = "CANDY";
                break;
            case 859:
                category = "FRITOS";
                break;
            case 860:
                category = "UPS";
                break;
            case 861:
                category = "DISCOUNT CODES";
                break;
            default:
                category = "UNKNOWN";
                break;
        }//end switch
        return category;
    }//end getCategory()
}//end class CategoryFinder
